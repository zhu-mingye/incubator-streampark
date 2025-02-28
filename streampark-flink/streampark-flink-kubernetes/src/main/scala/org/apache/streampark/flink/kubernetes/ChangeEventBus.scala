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

import com.google.common.eventbus.{AsyncEventBus, EventBus}

import java.util.concurrent.{LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit}

class ChangeEventBus {

  private val execPool = new ThreadPoolExecutor(
    Runtime.getRuntime.availableProcessors * 10,
    Runtime.getRuntime.availableProcessors * 20,
    60L,
    TimeUnit.SECONDS,
    new LinkedBlockingQueue[Runnable](2048))

  private[kubernetes] val asyncEventBus =
    new AsyncEventBus("[StreamPark][flink-k8s]AsyncEventBus", execPool)

  private[kubernetes] val syncEventBus = new EventBus("[StreamPark][flink-k8s]SyncEventBus")

  def postAsync(event: AnyRef): Unit = asyncEventBus.post(event)

  def postSync(event: AnyRef): Unit = syncEventBus.post(event)

  def registerListener(listener: AnyRef): Unit = {
    asyncEventBus.register(listener)
    syncEventBus.register(listener)
  }

}
