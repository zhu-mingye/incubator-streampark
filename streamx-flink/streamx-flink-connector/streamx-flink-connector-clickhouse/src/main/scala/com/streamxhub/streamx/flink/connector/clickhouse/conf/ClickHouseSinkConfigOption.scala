/*
 * Copyright (c) 2019 The StreamX Project
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.streamxhub.streamx.flink.connector.clickhouse.conf

import com.streamxhub.streamx.common.conf.ConfigOption
import com.streamxhub.streamx.common.util.ConfigUtils

import java.util.Properties
import scala.collection.JavaConverters._

/**
 * @author benjobs
 */
object ClickHouseSinkConfigOption {

  val CLICKHOUSE_SINK_PREFIX = "clickhouse.sink"

  /**
   *
   * @param properties
   * @return
   */
  def apply(prefixStr: String = CLICKHOUSE_SINK_PREFIX, properties: Properties = new Properties): ClickHouseSinkConfigOption = new ClickHouseSinkConfigOption(prefixStr, properties)

}


class ClickHouseSinkConfigOption(prefixStr: String, properties: Properties) extends Serializable {

  implicit val (prefix, prop) = (prefixStr, properties)

  val SIGN_COMMA = ","

  val hosts = ConfigOption[List[String]](
    key = "hosts",
    required = false,
    defaultValue = List(),
    classType = classOf[List[String]],
    handle = k => {
      properties
        .getProperty(k)
        .split(SIGN_COMMA)
        .filter(_.nonEmpty)
        .map(_.replaceAll("\\s+", "").replaceFirst("^http://|^", "http://"))
        .toList
    }
  )

  val user = ConfigOption(
    key = "user",
    required = true,
    classType = classOf[String]
  )

  val password = ConfigOption(
    key = "password",
    required = false,
    defaultValue = "",
    classType = classOf[String]
  )

  val failoverTable = ConfigOption(
    key = "failover.table",
    required = false,
    classType = classOf[String]
  )


  val targetTable = ConfigOption(
    key = "targetTable",
    required = false,
    defaultValue = "",
    classType = classOf[String]
  )


  val jdbcUrl = ConfigOption(
    key = "jdbcUrl",
    required = true,
    classType = classOf[String]
  )


  val driverClassName = ConfigOption(
    key = "driverClassName",
    required = false,
    defaultValue = null,
    classType = classOf[String]
  )

  val batchSize = ConfigOption(
    key = "batch.size",
    required = false,
    defaultValue = 1,
    classType = classOf[Int]
  )


  val batchDelaytime = ConfigOption(
    key = "batch.delaytime",
    required = false,
    defaultValue = 1000L,
    classType = classOf[Long],
    handle = k => {
      properties.remove(k).toString.toLong
    }
  )

  def getInternalConfig(): Properties = {
    ConfigUtils.getConf(prop.asScala.asJava, prefix)( "")
  }

}