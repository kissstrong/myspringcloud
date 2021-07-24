# myspringcloud
个人学习springcloud代码

所有的主要步骤：
建module
改pom
写yml
主启动
业务类
测试

没有run dashboard ：
首先打开  项目中.idea文件下的workspace.xml文件，找到Run Dashboard节点
添加如下代码
```xml
<option name="configurationTypes">  
      <set>  
        <option value="SpringBootApplicationConfigurationType" />  
      </set>  
 </option>  
```
重新再启动服务，Run Dashboard 就出来了
点击view -》tool windows 点击run dashboard即可

eureka集群之后，在集群服务8001 8002 ，80消费者调用的时候需要在resttemplate上加上负载均衡注解 @LoadBalanced
在80里面调用的时候写服务注册的名称，即在eureka上注册的名字，一般都是大写
```java
 //单点调用
//public static final String PAYMENTPATH="http://localhost:8001/";
/*轮询调用*/
public static final String PAYMENTPATH="http://CLOUD-PAYMENT-SERVICE";

```
调用即可（在80里面fetch-registry: true，否则报错）
eureka是CAP中的AP理论 宁可保存所有的服务，也不删除任何一个可能健康的服务

zookeeper和eureka差不多，这里没有写

consul 下载：https://www.consul.io/downloads
解压到对应的文件夹，执行consul命令：在文件位置的地址栏里面输入cmd 然后执行consul命令
```shell
Microsoft Windows [版本 10.0.18363.1556]
(c) 2019 Microsoft Corporation。保留所有权利。

D:\developertools\consul>consul
Usage: consul [--version] [--help] <command> [<args>]

Available commands are:
    acl            Interact with Consul's ACLs
    agent          Runs a Consul agent
    catalog        Interact with the catalog
    config         Interact with Consul's Centralized Configurations
    connect        Interact with Consul Connect
    debug          Records a debugging archive for operators
    event          Fire a new event
    exec           Executes a command on Consul nodes
    force-leave    Forces a member of the cluster to enter the "left" state
    info           Provides debugging information for operators.
    intention      Interact with Connect service intentions
    join           Tell Consul agent to join cluster
    keygen         Generates a new encryption key
    keyring        Manages gossip layer encryption keys
    kv             Interact with the key-value store
    leave          Gracefully leaves the Consul cluster and shuts down
    lock           Execute a command holding a lock
    login          Login to Consul using an auth method
    logout         Destroy a Consul token created with login
    maint          Controls node or service maintenance mode
    members        Lists the members of a Consul cluster
    monitor        Stream logs from a Consul agent
    operator       Provides cluster-level tools for Consul operators
    reload         Triggers the agent to reload configuration files
    rtt            Estimates network round trip time between nodes
    services       Interact with services
    snapshot       Saves, restores and inspects snapshots of Consul server state
    tls            Builtin helpers for creating CAs and certificates
    validate       Validate config files/directories
    version        Prints the Consul version
    watch          Watch for changes in Consul
```
出现该界面证明安装成功
启动：
命令 consul agent -dev 启动，看到Consul agent running! 启动成功
通过以下地址可以访问 Consul 首页
http://localhost:8500

## eureka 、zookeeper、consul三者比较

| 组件名       | 语言   | CAP  | 服务健康检查 | 对外暴露接口   | Spring CLoud 集成 |
| --------- | ---- | ---- | ------ | -------- | --------------- |
| Eureka    | Java | AP   | 可配支持   | HTTP     | 已集成             |
| Consul    | GO   | CP   | 支持     | HTTP/DNS | 已集成             |
| Zookeeper | Java | CP   | 支持     | 客户端      | 已集成             |

CAP原则又称CAP定理，指的是在一个[分布式系统]中， Consistency（一致性）、 Availability（可用性）、Partition tolerance（分区容错性），三者不可得兼。

一致性（C）：在[分布式系统]中的所有数据备份，在同一时刻是否同样的值。（等同于所有节点访问同一份最新的数据副本）

可用性（A）：保证每个请求不管成功或者失败都有响应。

分区容忍性（P）：系统中任意信息的丢失或失败不会影响系统的继续运作。 [1][ ]()

CAP原则的精髓就是要么AP，要么CP，要么AC，但是不存在CAP。

![https://bkimg.cdn.bcebos.com/pic/5bafa40f4bfbfbed9c15b19b72f0f736aec31f81?x-bce-process=image/resize,m_lfit,w_235,h_235,limit_1/format,f_auto](https://bkimg.cdn.bcebos.com/pic/5bafa40f4bfbfbed9c15b19b72f0f736aec31f81?x-bce-process=image/resize,m_lfit,w_235,h_235,limit_1/format,f_auto)