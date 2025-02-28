/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.streampark.flink.kubernetes

import org.apache.streampark.common.util.Logger
import org.apache.streampark.common.util.Utils._

import io.fabric8.kubernetes.api.model.{IntOrString, OwnerReferenceBuilder}
import io.fabric8.kubernetes.api.model.networking.v1beta1.IngressBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import org.apache.commons.io.FileUtils
import org.apache.flink.client.program.ClusterClient
import org.json4s.{DefaultFormats, JArray}
import org.json4s.jackson.JsonMethods.parse

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

import scala.collection.JavaConverters._
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

object IngressController extends Logger {

  def configureIngress(domainName: String, clusterId: String, nameSpace: String): Unit = {
    Try(new DefaultKubernetesClient) match {
      case Success(client) =>
        val annotMap = Map[String, String](
          "nginx.ingress.kubernetes.io/rewrite-target" -> "/$2",
          "nginx.ingress.kubernetes.io/proxy-body-size" -> "1024m",
          "nginx.ingress.kubernetes.io/configuration-snippet" -> ("rewrite ^(/" + clusterId + ")$ $1/ permanent;")
        )
        val labelsMap = Map[String, String](
          "app" -> clusterId,
          "type" -> "flink-native-kubernetes",
          "component" -> "ingress")

        val deployment = client
          .apps()
          .deployments()
          .inNamespace(nameSpace)
          .withName(clusterId)
          .get()

        val deploymentUid = if (deployment != null) {
          deployment.getMetadata.getUid
        } else {
          throw new RuntimeException(
            s"Deployment with name $clusterId not found in namespace $nameSpace")
        }

        // Create OwnerReference object
        val ownerReference = new OwnerReferenceBuilder()
          .withApiVersion("apps/v1")
          .withKind("Deployment")
          .withName(clusterId)
          .withUid(deploymentUid)
          .withController(true)
          .withBlockOwnerDeletion(true)
          .build()

        val ingress = new IngressBuilder()
          .withNewMetadata()
          .withName(clusterId)
          .addToAnnotations(annotMap.asJava)
          .addToLabels(labelsMap.asJava)
          .addToOwnerReferences(ownerReference) // Add OwnerReference
          .endMetadata()
          .withNewSpec()
          .addNewRule()
          .withHost(domainName)
          .withNewHttp()
          .addNewPath()
          .withPath(s"/$nameSpace/$clusterId/")
          .withNewBackend()
          .withServiceName(s"$clusterId-rest")
          .withServicePort(new IntOrString("rest"))
          .endBackend()
          .endPath()
          .addNewPath()
          .withPath(s"/$nameSpace/$clusterId" + "(/|$)(.*)")
          .withNewBackend()
          .withServiceName(s"$clusterId-rest")
          .withServicePort(new IntOrString("rest"))
          .endBackend()
          .endPath()
          .endHttp()
          .endRule()
          .endSpec()
          .build();
        client.network.ingress.inNamespace(nameSpace).create(ingress)
      case _ =>
    }
  }

  def configureIngress(ingressOutput: String): Unit = {
    close {
      val client = new DefaultKubernetesClient
      client.network.ingress
        .load(Files.newInputStream(Paths.get(ingressOutput)))
        .get()
      client
    }
  }

  private[this] def determineThePodSurvivalStatus(name: String, nameSpace: String): Boolean = {
    tryWithResource(KubernetesRetriever.newK8sClient()) {
      client =>
        Try {
          client
            .apps()
            .deployments()
            .inNamespace(nameSpace)
            .withName(name)
            .get()
            .getSpec()
            .getSelector()
            .getMatchLabels()
          false
        }.getOrElse(true)
    }
  }

  def ingressUrlAddress(
      nameSpace: String,
      clusterId: String,
      clusterClient: ClusterClient[_]): String = {
    val client = new DefaultKubernetesClient
    // for kubernetes 1.22+
    lazy val fromV1 =
      Option(client.network.v1.ingresses.inNamespace(nameSpace).withName(clusterId).get)
        .map(ingress => ingress.getSpec.getRules.get(0))
        .map(rule => rule.getHost -> rule.getHttp.getPaths.get(0).getPath)
    // for kubernetes 1.22-
    lazy val fromV1beta1 =
      Option(client.network.v1beta1.ingresses.inNamespace(nameSpace).withName(clusterId).get)
        .map(ingress => ingress.getSpec.getRules.get(0))
        .map(rule => rule.getHost -> rule.getHttp.getPaths.get(0).getPath)
    Try(
      fromV1
        .orElse(fromV1beta1)
        .map { case (host, path) => s"https://$host$path" }
        .getOrElse(clusterClient.getWebInterfaceURL)
    ).getOrElse(throw new RuntimeException("[StreamPark] get ingressUrlAddress error."))
  }

  @throws[IOException]
  def prepareIngressTemplateFiles(buildWorkspace: String, ingressTemplates: String): String = {
    val workspaceDir = new File(buildWorkspace)
    if (!workspaceDir.exists) workspaceDir.mkdir
    if (ingressTemplates.isEmpty) null;
    else {
      val outputPath = buildWorkspace + "/ingress.yaml"
      val outputFile = new File(outputPath)
      FileUtils.write(outputFile, ingressTemplates, "UTF-8")
      outputPath
    }
  }

}

case class IngressMeta(
    addresses: List[String],
    port: Integer,
    protocol: String,
    serviceName: String,
    ingressName: String,
    hostname: String,
    path: String,
    allNodes: Boolean)

object IngressMeta {

  @transient implicit lazy val formats: DefaultFormats.type = org.json4s.DefaultFormats

  def as(json: String): Option[List[IngressMeta]] = {
    Try(parse(json)) match {
      case Success(ok) =>
        ok match {
          case JArray(arr) =>
            val list = arr.map(
              x => {
                IngressMeta(
                  addresses =
                    (x \ "addresses").extractOpt[List[String]].getOrElse(List.empty[String]),
                  port = (x \ "port").extractOpt[Integer].getOrElse(0),
                  protocol = (x \ "protocol").extractOpt[String].getOrElse(null),
                  serviceName = (x \ "serviceName").extractOpt[String].getOrElse(null),
                  ingressName = (x \ "ingressName").extractOpt[String].getOrElse(null),
                  hostname = (x \ "hostname").extractOpt[String].getOrElse(null),
                  path = (x \ "path").extractOpt[String].getOrElse(null),
                  allNodes = (x \ "allNodes").extractOpt[Boolean].getOrElse(false)
                )
              })
            Some(list)
          case _ => None
        }
      case Failure(_) => None
    }
  }

}
