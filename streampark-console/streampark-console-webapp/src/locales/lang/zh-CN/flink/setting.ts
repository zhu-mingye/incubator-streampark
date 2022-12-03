/*
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
export default {
  settingTab: {
    systemSetting: '系统设置',
    alertSetting: '告警设置',
    flinkHome: 'Flink版本',
    flinkCluster: 'Flink集群',
  },
  systemSettingItems: {
    mavenSetting: {
      name: 'Maven配置',
    },
    dockerSetting: {
      name: 'Docker环境配置',
    },
    emailSetting: {
      name: '邮箱配置',
    },
    consoleSetting: {
      name: '控制台配置',
    },
    ingressSetting: {
      name: 'k8s Ingress 配置',
    },
  },
  flink: {
    flinkName: 'Flink名称',
    flinkNamePlaceholder: '请输入Flink别名',
    flinkHome: '安装路径',
    flinkHomePlaceholder: '请输入Flink安装路径',
    description: '描述',
    descriptionPlaceholder: 'Flink描述',
    operateMessage: {
      flinkNameTips: 'Flink别名,举例: Flink-1.12',
      flinkNameIsUnique: 'Flink名称已存在',
      flinkNameIsRequired: 'Flink名称必填',
      flinkHomeTips: 'Flink所在服务器的绝对路径,举例: /usr/local/flink',
      flinkHomeIsRequired: 'Flink安装路径必填',
      createFlinkHomeSuccessful: ' 创建成功!',
      updateFlinkHomeSuccessful: ' 更新成功!',
    },
  },
  alert: {
    alertSetting: '告警设置',
    alertName: '告警名称',
    alertNamePlaceHolder: '请输入告警名称',
    alertNameTips: '告警名称, 举例: StreamPark 组告警',
    alertNameErrorMessage: {
      alertNameIsRequired: '告警名称必填',
      alertNameAlreadyExists: '告警名称必须唯一. 当前输入的名称已存在',
      alertConfigFailed: '错误出现 ,原因: ',
    },
    faultAlertType: '故障告警类型',
    faultAlertTypeIsRequired: '故障告警类型必选',
    email: '电子邮箱',
    alertEmail: '告警邮箱',
    alertEmailAddressIsRequired: '邮箱地址必填',
    alertEmailFormatIsInvalid: '(邮箱)格式有误',
    alertEmailPlaceholder: '请输入邮箱，多个邮箱用逗号(,)隔开',
    dingTalk: '钉钉',
    dingTalkUrl: '钉钉Url',
    dingTalkUrlFormatIsInvalid: '(钉钉Url)格式有误',
    dingTalkPlaceholder: '请输入钉钉Url',
    dingtalkAccessToken: '访问令牌',
    dingtalkAccessTokenPlaceholder: '请输入钉钉访问令牌',
    secretEnable: '启用密钥令牌',
    secretTokenEnableHelpMessage: '钉钉密钥令牌是否启用',
    secretToken: '密钥令牌',
    secretTokenPlaceholder: '请输入密钥令牌',
    dingTalkSecretTokenIsRequired: '钉钉密钥令牌必填',
    dingTalkUser: '钉钉消息接受者',
    dingTalkUserPlaceholder: '请输入钉钉消息接受者',
    dingtalkIsAtAll: '(通知)所有',
    whetherNotifyAll: '是否(通知)所有(消息接收者)',
    weChat: '微信',
    weChattoken: '微信令牌',
    weChattokenPlaceholder: '请输入微信令牌',
    weChattokenIsRequired: '微信令牌必填',
    sms: '短信',
    smsPlaceholder: '请输入手机号',
    mobileNumberIsRequired: '请输入手机号',
    smsTemplate: '短信模板',
    smsTemplateIsRequired: '短信模板必填',
    lark: '飞书',
    larkToken: '飞书令牌',
    larkTokenPlaceholder: '请输入飞书令牌',
    larkIsAtAll: '(通知)所有',
    larkSecretEnable: '启用飞书密钥令牌',
    larkTokenEnableHelpMessage: '飞书密钥令牌是否启用',
    larkSecretToken: '飞书密钥令牌',
    larkSecretTokenPlaceholder: '请输入飞书密钥令牌',
    larkSecretTokenIsRequired: '飞书密钥令牌必填',
    alertDetail: '告警(配置)详情',
    alertOperationMessage: {
      updateAlertConfigFailed: '告警配置更新失败!',
      updateAlertConfigSuccessfull: '告警配置更新成功!',
    },
    delete: '是否确定删除此警报 ?',
  },
  cluster: {
    detail: '查看集群详情',
    stop: '停止集群',
    start: '开启集群',
    edit: '编辑集群',
    delete: '确定要删除此集群 ?',
    form: {
      clusterName: '集群名称',
      executionMode: '执行模式',
      versionId: 'Flink版本',
      addType: '添加类型',
      addExisting: '已有集群',
      addNew: '全新集群',
      yarnQueue: 'Yarn队列',
      address: '集群地址',
      yarnSessionClusterId: 'Yarn Session模式集群ID',
      k8sNamespace: 'k8s命名空间',
      k8sClusterId: 'k8s集群ID',
      serviceAccount: 'k8s命名空间绑定的服务账号',
      k8sConf: 'k8s环境Kube配置文件',
      flinkImage: 'Flink基础docker镜像',
      k8sRestExposedType: 'K8S服务对外类型',
      resolveOrder: '类加载顺序',
      taskSlots: '任务槽数',
      jmOptions: 'JM内存',
      tmOptions: 'TM内存',
      dynamicProperties: '动态参数',
      clusterDescription: '集群描述',
    },
    placeholder: {
      addType: '请选择集群添加类型',
      clusterName: '请输入集群名称',
      executionMode: '请选择执行模式',
      versionId: '请选择Flink版本',
      yarnQueue: '请选择Yarn队列',
      addressRemoteMode:
        '请输入集群地址，多个地址使用英文逗号分割，例如：http://host:port,http://host1:port2',
      addressNoRemoteMode: '请输入集群地址，例如：http://host:port',
      yarnSessionClusterId: '请输入Yarn Session模式集群ID',
      k8sConf: '示例：~/.kube/config',
      flinkImage: '请输入Flink基础docker镜像的标签，如：flink:1.13.0-scala_2.11-java8',
      k8sRestExposedType: 'kubernetes.rest-service.exposed.type',
      resolveOrder: 'classloader.resolve-order',
      taskSlots: '每个TaskManager的插槽数',
      totalOptions: '总内存',
      jmOptions: '请选择要设置的jm资源参数',
      tmOptions: '请选择要设置的tm资源参数',
      clusterDescription: '请输入对该申请的描述',
    },
    required: {
      address: '必须填写集群地址',
      executionMode: '执行模式必填',
      clusterId: 'Yarn Session Cluster 为必填项',
      versionId: 'Flink 版本必选',
      flinkImage: 'link基础docker镜像是必填的',
      resolveOrder: '类加载顺序必选',
    },
    operateMessage: {
      createFlinkSessionClusterSuccessful: ' 创建成功!',
      createFlinkSessionClusterFailed: 'session集群创建失败, 请检查日志',
      hadoopEnvInitializationFailed: 'Hadoop环境初始化失败，请检查环境设置',
      flinkClusterIsStarting: '当前集群正在启动',
      flinkClusterHasStartedSuccessful: '当前集群已成功启动',
      updateFlinkClusterSuccessful: ' 更新成功!',
    },
    view: {
      clusterId: '集群ID',
    },
  },
  env: {
    conf: 'Flink 配置',
    sync: '配置同步',
  },
};
