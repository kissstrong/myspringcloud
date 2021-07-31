# myspringcloud
个人学习springcloud代码

| 功能     | 框架                                |
| ------ | --------------------------------- |
| 服务注册中心 | （X）Eureka 、zookeeper、consul、nacos |
| 服务调度   | ribbon、LoadBalance                |
| 服务调度2  | （X）Feign、openfeign                |
| 服务降级   | （X）Hystrix、resilience4j、sentinel  |
| 服务网关   | （X）Zuul、（！）Zuul2、gateway          |
| 服务配置   | （X）config、nacos                   |
| 服务总线   | （X）Bus、Nacos                      |



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
#spring cloud
##服务注册与发现
###eureka
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
###zookeeper
zookeeper和eureka差不多，这里没有写
###consul
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

### eureka 、zookeeper、consul三者比较

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

##负载均衡 ribbon
先使用eureka的集群模式，恢复原来的集群环境

***LB负载均衡(Load Balance)是什么***

简单的说就是将用户的请求平摊的分配到多个服务上，从而达到系统的HA(高可用)。常见的负载均衡有软件Nginx，LVS，硬件F5等。

***Ribbon本地负载均衡客户端VS Nginx服务端负载均衡区别***

***Nginx是服务器负载均衡***，客户端所有请求都会交给nginx，然后由nginx实现转发请求。即负载均衡是由服务端实现的。

***Ribbon本地负载均衡***，在调用微服务接口时候，会在注册中心上获取注册信息服务列表之后缓存到VM本地，从而在本地实现RPC远程服务调用技术。
使用：不需要映入依赖，因为映入eureka依赖的时候里面自带ribbon依赖
```xml
 <!--引入eureka-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
修改负载均衡的方法:ribbon中有个Irule接口
服务调度方法：
- RoundRobinRule轮询（默认）
- RandomRule随机
- RetryRule轮询重试（重试采用的默认也是轮询）
- WeightedResponseTimeRule响应速度决定权重
- BestAvailableRule最优可用（底层也有RoundRobinRule）
- AvailabilityFilteringRule可用性过滤规则（底层也有RoundRobinRule）
- ZoneAvoidanceRule区域内可用性能最优
- ZoneAvoidanceRule区域内可用性能最优
  也可以自定义负载均衡规则，模仿它们的写法，继承RoundRibbonRule，重写一些方法，加上自己的Server即可
  如何修改调度方法：
  一定要在主启动类扫描的外面写，否则全部被主启动类里面的componentScan注解扫描，会变成全局配置
  以80模块为例，可以在com包里面在建立一个新包myrule
  建立一个MySelfRule配置类

```java
@Configuration
public class MyselfRule {

    @Bean
    public IRule myRule(){
        return new RandomRule(); //定义为随机
    }
}
```
在主启动类上加上注解RibbonClient
```java
@SpringBootApplication
@EnableSwagger2 //name表示访问的服务名称 configuration表示遵循的调度配置
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = MyselfRule.class)
public class Application80 {
    public static void main(String[] args) {
        SpringApplication.run(Application80.class,args);
    }
}
```
负载均衡算法(轮询): rest接口第几次请求数%服务器集群总数量=实际调用服务器位置下标，每次服务重启动后rest接口计数从1开始。
List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
如:List [0] instances = 127.0.0.1:8002
   List [1]instances = 127.0.0.1:8001
8001+ 8002组合成为集群，它们共计2台机器，集群总数为2，按照轮询算法原理:
当总请求数为1时:1%2=1对应下标位置为1，则获得服务地址为127.0.0.1:8001
当总请求数位2时:2%2=О对应下标位置为0，则获得服务地址为127.0.0.1:8002
当总请求数位3时:3%2=1对应下标位置为1，则获得服务地址为127.0.0.1:8001
当总请求数位4时:4%2=О对应下标位置为0，则获得服务地址为127.0.0.1:8002
如此类推.....
## 服务调度

###openFeign

Feign能于什么

Feign旨在使编写Java Http客户端变得更容易。

前面在使用Ribbon+RestTemplate时，利用RestTemplate对http请求的封装处理，形成了一套模版化的调用方法。但是在实际开发中，由于对服务依赖的调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装一些客户端类来包装
这些依赖服务的调用。所以，Feign在此基础上做了进一步封装，由他来帮助我们定义和实现依赖服务接口的定义。在Feign的实现下，我们只需创建一个接口并使用注解的方式来配置它(以前是Dao接口上面标注Mapper注解,现在是一个微服务接口上面标注一个
Feign注解即可)，即可完成对服务提供方的接口绑定，简化了使用Spring cloud Ribbon时，自动封装服务调用客户端的开发量。

Feign集成了Ribbon

利用Ribbon维护了Payment的服务列表信息，并且通过轮询实现了客户端的负载均衡。而与Ribbon不同的是，通过feign只需要定义服务绑定接口且以声明式的方法，优雅而简单的实现了服务调用

feign和哦openfeign的区别:

feign:
Feign是Spring Cloud组件中的一个轻量级RESTful的HTTP服务客户端Feign内置了Ribbon，用来做客户端负载均衡，去调用服务注册中心的服务。Feign的使用方式是:使用Feign的注解定义接口，调用这个接口，就可以调用服务注册中心的服务
```xml
<dependency>
    <groupid>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</ dependency>
```

openfeign: OpenFeign是Spring Cloud在Feign的基础上支持了SpringMVC的注解，如@RequesMapping等等。OpenFeign的@FeignClient可以解析SpringMVC的@RequestMapping注解下的接口，并通过动态代理的方式产生实现类，实现类中做负载均衡并调用其他服务。

```xml
<dependency>
    <groupid>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
使用方法：导入上面的依赖
在启动类上加注解@EnableFeignClients 
在service上加注解@FeignClient ,这边注解里面的value是调的服务的名称 ，下面的是对应的controller，调的就是这个请求路劲下的接口
```java
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {
    @GetMapping("/provider/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id);
}
```

feign会有一个超时时间，需要在客户端利配置一下
```yaml
#设置feign客户端超时时间
ribbon:
  #指定建立连接时间
  ReadTimeout: 5000
  #指定读取服务的时间
  ConnectTimeout: 5000

```

feign的日志增强：
添加配置：导入的Logger的包不能错，要是feign里面的
```java
package com.cyz.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cyz
 * @date 2021/7/25 0025 12:58
 */
@Configuration
public class FeignLogConfig {

    @Bean
    Logger.Level FeignLog(){
        return Logger.Level.FULL;
    }
}
```
```yaml
#feign的日志增强
logging:
  level: #下面写的是feign的接口位置
    com.cyz.service.PaymentFeignService: debug
```
检测：调用feign的接口，控制台会打印信息，很详细
```shell
2021-07-25 13:03:45.653 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] <--- HTTP/1.1 200 (442ms)
2021-07-25 13:03:45.653 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] connection: keep-alive
2021-07-25 13:03:45.653 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] content-type: application/json
2021-07-25 13:03:45.653 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] date: Sun, 25 Jul 2021 05:03:45 GMT
2021-07-25 13:03:45.653 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] keep-alive: timeout=60
2021-07-25 13:03:45.653 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] transfer-encoding: chunked
2021-07-25 13:03:45.653 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] 
2021-07-25 13:03:45.654 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] {"code":200,"message":"查询的结果8001","data":{"id":1,"serial":"张三"}}
2021-07-25 13:03:45.655 DEBUG 13132 --- [p-nio-80-exec-5] com.cyz.service.PaymentFeignService      : [PaymentFeignService#getPaymentById] <--- END HTTP (81-byte body)
2021-07-25 13:03:46.346  INFO 13132 --- [erListUpdater-0] c.netflix.config.ChainedDynamicProperty  : Flipping property: CLOUD-PAYMENT-SERVICE.ribbon.ActiveConnectionsLimit to use NEXT property: niws.loadbalancer.availabilityFilteringRule.activeConnectionsLimit = 2147483647

```
##服务熔断
服务雪崩
多个微服务之间调用的时候，假设微服务A调用微服务B和微服务C，微服务B和微服务C又调用其它的微服务，这就是所谓的“扇出”。如果扇出的链路上某个微服务的调用响应时间过长或者不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃。
###Hystrix
Hystrix是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖出问题的情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性。
"断路器”本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝)，向调用方返回一个符合预期的、可处理的备选响应(FallBack)，而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间、不必要地占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。
####服务降级
服务器忙，请稍后再试，不让客户端等待并立刻返回个友好提示，fallback
情况：
  程序运行异常
  超时
  服务熔断触发服务降级
  线程池/信号量打满也会导致服务降级


####服务熔断
类比保险丝达到最大服务访问后，直接拒绝访问，拉闸限电，然后调用服务降级的方法并返回友好提示
就是保险丝
  服务的降级->进而熔断->恢复调用链路

####服务限流
秒杀高并发等操作，严禁一窝蜂的过来拥挤，大家排队，一秒钟N个，有序进行

新建服务模块cloud-provider-hystrix-payment8001

引入hystrix依赖
```xml
<!--引入hystrix-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
创建两个请求 一个正常，一个等待3秒
```java
@Service
public class PaymentService {

    public String ok(){
        return "好的";
    }
    public String error(){
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            return "不好的";
        }
    }
}

```
```java
public class PaymentController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @ApiOperation(value="查询数据", notes="查询数据")
    @GetMapping("feign/payment/ok")
    public String getPayment(){

        String ok = paymentFeignService.ok();
        return ok;
    }

    @GetMapping("feign/payment/error")
    public String getPaymentError(){

        return paymentFeignService.error();
    }
}
```
使用jmeter测试 自己安装 添加线程组 发20000次请求  右键线程组 添加-》取样器-》http请求  运作即可

对方服务(8001)超时了，调用者(80)不能一直卡死等待，必须有服务降级
对方服务(8001)down机了，调用者(80)不能一直卡死等待，必须有服务降级
对方服务(8001)OK，调用者(80)自己出故障或有自我要求(自己的等待时间小于服务提供者)

***降级配置：*** 

先从8001自身找办法 设置自身调用超时时间的峰值，峰值内可以正常运行，超过了需要有兜底的方法处理，作服务降级fallback
方法一:在8001里面降级

8001fallback：
  在主启动类上加上注解@EnableCircuitBreaker，在service的方法里面加上注解@HystrixCommand 设置兜底方法
具体代码如下：
```java
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker//开启服务降级
public class PaymentHystrix8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrix8001.class,args);
    }
}
```
```java
@Service
public class PaymentService {

    public String ok(){
        return "好的";
    }

    @HystrixCommand(fallbackMethod = "error_handler",commandProperties = {//设置超时时间，超时就调用兜底方法
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String error(){
        int a = 10/0;
        try {
            //TimeUnit.SECONDS.sleep(5);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            return "不好的";
        }
    }
    public String error_handler(){
        return "我是兜底方法，上面的方法超时或出错";
    }
}
```

方法二：在80里面降级
80fallback：引入以依赖
```xml
<!--引入hystrix-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
开启配置：
```yaml
feign:
  hystrix:
    enabled: true
```
启动类开启注解：
```java
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients
@EnableHystrix//开启降级配置
public class ApplicationFeignHystrix80 {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationFeignHystrix80.class,args);
    }
}
```
controller这边调用写也加上超时回调，时间写1.5秒,服务那边写3秒超时
```java
 @GetMapping("feign/payment/error")
    @HystrixCommand(fallbackMethod = "error_handler",commandProperties = {//设置超时时间，超时就调用兜底方法
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
    public String error(){
       return paymentFeignService.error();
    }
    public String error_handler(){
        return "我是兜底方法80，上面的方法超时或出错";
    }
```
这边有个问题：每个请求都有一个回调方法，代码量很庞大
在controller上加上该注解，在方法里面没有fallBackMethod属性时，取默认 
@DefaultProperties(defaultFallback = "")
1:1每个方法配置一个服务降级方法，技术上可以，实际上傻X
1:N除了个别重要核心业务有专属，其它普通的可以通过@DefaultProperties(defaultFalback = "")统一跳转到统─处理结果页面
实例：
```java
@DefaultProperties(defaultFallback = "global_fallBack_Method")

@GetMapping("feign/payment/global")
@HystrixCommand(commandProperties = {//设置超时时间，超时就调用兜底方法
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
})
public String TestGlobal(){
        int i =10/0;

        return "ok";
        }
public String global_fallBack_Method(){
        return "我是全局回调方法";
        }
```
针对服务配置回调方法：
新建一个实现类PaymentFeignServiceImpl 实现PaymentFeignService接口加上@component注解
```java
@Component
public class PaymentFeignServiceImpl implements PaymentFeignService {
    @Override
    public String ok() {
        return "我是OK的回调方法";
    }

    @Override
    public String error() {
        return "我是Error的回调方法";
    }
}
```
在PaymentFeignService的FeignClient注解里面加上fallback = PaymentFeignServiceImpl.class
```java
@Component
@FeignClient(value = "CLOUD-HYSTRIX-PAYMENT-SERVICE",fallback = PaymentFeignServiceImpl.class)
public interface PaymentFeignService {
    @GetMapping("/ok")
    public String ok();

    @GetMapping("/error")
    public String error();
}

```
测试，当8001挂掉的时候就会回调impl里面的方法

***服务熔断***
熔断机制概述
熔断机制是应对雪崩效应的一种微服务链路保护机制。当扇出链路的某个微服务出错不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回错误的响应信息。
当检测到该节点微服务调用响应正常后，***恢复调用链路***。

在Spring Cloud框架里，熔断机制通过Hystrix实现。Hystrix会监控微服务间调用的状况
当失败的调用到一定阈值，缺省是5秒内20次调用失败，就会启动熔断机制。熔断机制的注解是@HystrixCommand。

编写service
```java
    //服务熔断  所有能配置的都在HystrixCommandProperties里面
    @HystrixCommand(fallbackMethod = "testRonDuang_fallBack" , commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启熔断器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//最近10秒
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//失败率达到多少跳闸
            })
    public String testRonDuang(int id){
        if (id<0){
            throw new RuntimeException("id不能小于0");
        }
        return Thread.currentThread().getName()+"It"+"调用成功，流水号:";
    }
    public String testRonDuang_fallBack(int id){
        return "熔断回调id不能为负数";
    }
```
编写controller
```java
@GetMapping("/testronduan")
    public String testronduan(int id){

        return paymentService.testRonDuang(id);
    }
```
测试：多次输入负数，10次以上，如后输入正数也会报错，服务不可用，待过段时间正确率上升了，服务就可以使用了
熔断类型：
  熔断打开：请求不再进行调用当前服务，内部设置时钟一般为MTTR(平均故障处理时间)，当打开时长达到所设时钟则进入半熔断状态
  熔断关闭：熔断关闭不会对服务进行熔断
  熔断半开：部分请求根据规则调用当前服务，如果请求成功且符合规则则认为当前服务恢复正常，关闭熔断

涉及到断路器的三个重要参数:快照时间窗、请求总数阀值、错误百分比阀值。
1:快照时间窗:断路器确定是否打开需要统计一些请求和错误数据，而统计的时阐范围就是快照时间窗，默认为最近的10秒。
2∶请求总数阀值:在快照时间窗内，必须满足请求总数阀值才有资格熔断。默认为20，意味着在10秒内，如果该hystrix命令的调用次数不足20次,即使所有的请求都超时或其他原因失败，断路器都不会打开。
3:错误百分比阀值:当请求总数在快照时间窗内超过了阀值，比如发生了30次调用，如果在这30次调用中，有15次发生了超时异常，也就是超过50%的错误百分比，在默认设定50%阀值情况下，这时候就会将断路器打开。

***hystrix服务监控（可视化界面）***
建立新模块cloud-sonsumer-hystrix-dashboard9001 
导入依赖:（需要监控的模块都要导入spring-boot-starter-actuator依赖才行）
```xml
 <!--引入hystrix-dashboard-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
<!--可视化-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```
启动类上加上注解@EnableHystrixDashboard开启监控，启动服务器，访问地址是：
http://localhost:9001/hystrix 出现一个可视化界面即操作成功

**使用**
在使用hystrix的模块里面的启动类里面加入bean（不要问为什么）
```java

    /*此配置是为了服务监控而配置，与服务容错本身无关，
    springcLoud升级后的玩IServletRegistrationBean
    因为springboot的默认路径不是"/hystrix.stream"，
    只要在自己的项目里配置上下面的servlet就可以了
    */
    @Bean
    public ServletRegistrationBean getservlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings( "/hystrix.stream" ) ;
        registrationBean. setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
```
这个一定要引入spring-boot-starter-actuator才行，访问该服务使用熔断器的请求，然后再9001监控页面输入
http://localhost:8001/hystrix.stream即可查看访问

## 网关

###gateway

Gateway是在Spring生态系统之上构建的API网关服务，基于Spring 5,Spring
Boot 2和Project Reactor等技术。
Gateway旨在提供一种简单而有效的方式来对API进行路由，以及提供一些强大的过滤器功能，例如:熔断、限流、重试等

SpringCloud Gateway使用的Webflux中的reactor-netty响应式编程组件，底层使用了Netty通讯框架。
可以做：反向代理 鉴权  流量控制  熔断 日志监控

三大核心概念：
Route(路由)
  路由是构建网关的基本模块，它由ID，目标URI，一系列的断言和过滤器组成，如果断言为true则匹配该路由
Predicate(断言)
  参考的是Java8的java.util.function.Predicate，开发人员可以匹配HTTP请求中的所有内容(例如请求头或请求参数)，如果请求与断言相匹配则进行路由
Filter(过滤）
  指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由前或者之后对请求进行修改。
核心逻辑
路由转发+执行过滤器链
***使用***
建立cloud-gateway-gateway9527模块，导入依赖（不要导入web 和actuator）
```xml
<dependencies>
        <!--gateway-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!--引入eureka-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
写配置：
```yaml
server:
  port: 9527
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001 #匹配后提供服务的路由地址
          predicates:
            - Path=/payment/get/** #断言，路径相匹配的进行路由

        - id: payment_routh2 #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001 #匹配后提供服务的路由地址
          predicates:
            - Path=/payment/lb/** #断言，路径相匹配的进行路由

eureka:
  client:
    register-with-eureka: true #false表示自己是服务中心，不需要注册自己
    service-url:
      #设置与Eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://localhost:7001/eureka
      #defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
    fetch-registry: true
  instance:
    hostname: cloud-gateway-service
```
写启动类
```java
@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication9527 {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication9527.class,args);
    }
}
```
*** 测试 **

在cloud-provider-payment8001的controller里面加入两个请求
```java

    @ApiOperation(value="测试payment/get/{id}", notes="测试feign的超时")
    @GetMapping("/payment/get/{id}")
    public String payment(@PathVariable String id){
       return "8001get"+id;
    }
    @ApiOperation(value="测试payment/lb", notes="测试payment/lb")
    @GetMapping("/payment/lb/{id}")
    public String payment1(@PathVariable String id){
        return "8001lb"+id;
    }

```
启动7001 8001 9527 ，使用http://localhost:9527/payment/lb/1访问就会调用lb的请求
http://localhost:9527/payment/get/1就会调用get的请求，就会隐藏掉8001端口
配置路由两种方式：
 - 在yml中配置，如上
 - 在代码中注入RouteLocator的bean 如下
  写一个配置：
```java
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator getMyGateway(RouteLocatorBuilder routeLocatorBuilder){
        //这边获得的routes就类似于配置文件那边的routes
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //https://news.baidu.com/guonei 访问国内新闻试试
         //这里的id和yml中的id类似 patterns类似于yml中的predicates断言  uri就类似于yml中的uri
        //整体就是访问9527的guonei，就会访问https://news.baidu.com/guonei
        routes.route("com_cyz",r->r.path("/guonei").uri("https://news.baidu.com/guonei")).build();

        return routes.build();
    }
}
```
***集群网关路由***
写服务名，调用服务
```yml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true #开启从注册中心动态创建路由功能，利用微服务名进行路由
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          #uri: http://localhost:8001 #匹配后提供服务的路由地址 写死的
          uri: lb://CLOUD-PAYMENT-SERVICE #匹配后提供服务的路由地址  路由的 这边的lb可以理解为loadbalance
          predicates:
            - Path=/payment/get/** #断言，路径相匹配的进行路由

        - id: payment_routh2 #路由的ID，没有固定规则但要求唯一，建议配合服务名
          #uri: http://localhost:8001 #匹配后提供服务的路由地址
          uri: lb://CLOUD-PAYMENT-SERVICE #匹配后提供服务的路由地址 路由的 这边的lb可以理解为loadbalance
          predicates:
            - Path=/payment/hh/** #断言，路径相匹配的进行路由
```
断言的多种方式，在控制台有输出
```shell
Loaded RoutePredicateFactory [After]
Loaded RoutePredicateFactory [Before]
Loaded RoutePredicateFactory [Between]
Loaded RoutePredicateFactory [Cookie]
Loaded RoutePredicateFactory [Header]
Loaded RoutePredicateFactory [Host]
Loaded RoutePredicateFactory [Method]
Loaded RoutePredicateFactory [Path]
Loaded RoutePredicateFactory [Query]
Loaded RoutePredicateFactory [ReadBodyPredicateFactory]
Loaded RoutePredicateFactory [RemoteAddr]
Loaded RoutePredicateFactory [Weight]
Loaded RoutePredicateFactory [CloudFoundryRouteService]
```
常用的有如下几种：
1.After Route Predicate 在这个时间之后起作用
2.Before Route Predicate 在这个时间之前起作用
3.Between Route Predicate
4.Cookie Route Predicate
5.Header Route Predicate
6. Host Route Predicate
  7.Method Route Predicate
  8.Path Route Predicate
  9.Query Route Predicate
  总结：说白了，Predicate就是为了实现一组匹配规则，让请求过来找到对应的Route进行处理。
```yml
 - id: payment_routh2 #路由的ID，没有固定规则但要求唯一，建议配合服务名
          #uri: http://localhost:8001 #匹配后提供服务的路由地址
          uri: lb://CLOUD-PAYMENT-SERVICE #匹配后提供服务的路由地址 路由的 这边的lb可以理解为loadbalance
          predicates:
            - Path=/payment/hh/** #断言，路径相匹配的进行路由
            #- After=2021-07-30T10:48:39.219+08:00[Asia/Shanghai] #通过这个获取 ZonedDateTime now = ZonedDateTime.now();
            #- Before=2021-07-30T10:48:39.219+08:00[Asia/Shanghai]
            #- Between=2021-07-30T10:48:39.219+08:00[Asia/Shanghai],2021-07-30T10:49:39.219+08:00[Asia/Shanghai]
            #- Cookie=username,cyz #带Cookie 且key有username value 是cyz
            #- Header=X-Request-Id, \d+   #两个参数:一个是属性名称和一个正则表达式，这个属性值和正则表达式匹配则执行。header中要有 X-Request-Id属性，且为整数
            #- Host=**.cyz.com  #请求头要带**.cyz.com才行
            #- Method=GET #GET请求才可以
            #- Query=username, \d+ #参数要有username 且值为整数
```
***过滤器***
官网使用的太为繁琐，一般使用自定义过滤器，只需要实现两个接口即可GlobalFilter, Ordered,可以配置多个
```java
@Component
public class MyLogGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        if (uname==null){ //判断是否有uname参数
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            System.out.println("没有携带uname");
            return exchange.getResponse().setComplete();
        }
        System.out.println("携带uname");
        return chain.filter(exchange);
    }
//下面这个是顺序，0代表优先级，范围int  -2147483648  -- 2147483647;
    @Override
    public int getOrder() {
        return 0;
    }
}

http://localhost:9527/payment/hh/1?uname=123132
```
## 配置

###springcloudconfig

是什么：
   SpringCloud Config为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供了一个中心化的外部配置。
怎么玩：
  SpringCloud Config分为服务端和客户端两部分。
  服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密/解密信息等访问接口
  客户端则是通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行版本管理，并且可以通过git客户端工具来方便的管理和访问配置内容
干什么：
  集中管理配置文件
  不同环境不同配置，动态化的配置更新，分环境部署比如dev/test/prod/beta/release
  运行期间动态调整配置，不再需要在每个服务部署的机器上编写配置文件，服务会向配置中心统一拉取配置自己的信息
  当配置发生变动时，服务不需要重启即可感知到配置的变化并应用新的配置
  将配置信息以REST接口的形式暴露

***具体使用***
先在github上面创建仓库 springcloud-config 里面加入三个yml
config-dev.yml
config-prod.yml
config-test.yml
创建服务端：cloud-config-center-3344模块 
导入依赖 spring-cloud-config-server
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <!--引入eureka-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

    </dependencies>
```
写配置：
```yml
server:
  port: 3344
spring:
  application:
    name: cloud-config-center
  cloud:
    config:
      server:
        git:
          uri: https://github.com/kissstrong/springcloud-config.git #github上面的地址
          #搜索目录
          search-paths:
            - springcloud-config
      #读取的分支
      label: master

eureka:
  client:
    register-with-eureka: true #false表示自己是服务中心，不需要注册自己
    service-url:
      #设置与Eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka7001.com:7001/eureka #单机版
      #集群版本
      #defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
config:
  name: cyz
```
写启动类

````java
@SpringBootApplication
@EnableConfigServer
public class ConfigCenter3344 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigCenter3344.class,args);
    }
}
````
测试启动7001 3344 访问
http://localhost:3344/master/config-dev.yml
http://localhost:3344/master/config-prod.yml
http://localhost:3344/master/config-test.yml
都可以访问到对应的yml

读取策略：
/{label}/{application}-{profile}.yml 读取对应分支下的对应yml
http://localhost:3344/master/config-test.yml
/{application}-{profile}.yml  读取在application.yml中配置的label即master下的yml
http://localhost:3344/config-test.yml
/{application}/profile}/{label}
http://localhost:3344/config/test/master
创建客户端：cloud-config-client-3355 
了解：
  applicaiton.yml是用户级的资源配置项
  bootstrap.yml是系统级的，优先级更加高
  Spring Cloud会创建一个“Bootstrap context”，作为Spring应用的`Application Context的父上下文。初始化的时候，‘BootstrapContext`负责从外部源加载配置属性并解析配置。这两个上下文共享一个从外部获取的Environment'
  Bootstrap`属性有高优先级，默认情况下，它们不会被本地配置覆盖。`Bootstrap context和Application Context有着不同的约定，所以新增了一个'bootstrap.yml'文件，保证`Bootstrap Context`和`Application Context`配置的分离。
  要将Client模块下的application.yml文件改为bootstrap.yml,这是很关键的，
  因为bootstrap.yml是比application.yml先加载的。bootstrap.yml优先级高于application.yml
导依赖：
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <!--引入eureka-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

    </dependencies>
```
写配置bootstrap.yml
```yml
server:
  port: 3355
spring:
  application:
    name: cloud-config-client
  cloud:
    #config客户端配置
    config:
      label: master #分支名
      name: config #读取的配置文件名
      profile: dev #后缀名称
      uri: http://localhost:3344 #配置中心地址

eureka:
  client:
    register-with-eureka: true #false表示自己是服务中心，不需要注册自己
    service-url:
      #设置与Eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka7001.com:7001/eureka #单机版
      #集群版本
      #defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
```
启动类：
```java
@SpringBootApplication
@EnableEurekaClient
public class ConfigClient3355 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClient3355.class,args);
    }
}
```
controller:
```java
@RestController
public class TestConfigController {

    @Value("${config.name}")
    private String name;

    @RequestMapping("getname")
    private String get(){
        return name;
    }
}

```
测试：启动7001 3344 3355 访问http://localhost:3355/getname 看能否得到值

***配置刷新问题***
服务端可以动态刷新，客户端不可以
解决办法：
在客户端的bootstrap.yml里面加入配置，暴露端点（前提要引入spring-boot-starter-actuator）
```yml
#暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
加上@RefreshScope注解，目前只支持在bean上加，不知道为啥，对应的controller也改变

```java
@Component
@RefreshScope
public class User {
    @Value("${config.name}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```
````java
@RestController
public class TestConfigController {

    @Autowired
    private User user;

    @RequestMapping("getname")
    private String get(){
        return user.getName();
    }
}
````
测试，访问http://localhost:3355/getname  
修改GitHub上配置文件的值，然后再发一个请求刷新一下即可http://localhost:3355/actuator/refresh  的post请求
在访问http://localhost:3355/getname 发现值已经刷新
## 技术总线

###springcloud bus

分布式自动刷新配置功能
Spring Cloud Bus配合Spring Cloud Config使用可以实现配置的动态刷新。
Bus支持两种消息代理: RabbitMQ和Kafka

什么是总线
  在微服务架构的系统中，通常会使用轻量级的消息代理来构建一个共用的消息主题，并让系统中所有微服务实例都连接上来。
  由于该主题中产生的消息会被所有实例监听和消费，所以称它为消息总线。
  在总线上的各个实例，都可以方便地广播━些需要让其他连接在该主题上的实例都知道的消息。
基本原理
  ConfigClient实例都监听MQ中同一个topic(默认是springCloudBus)。
  当一个服务刷新数据的时候，它会把这个信息放入到Topic中，这样其它监听同一Topic的服务就能得到通知，然后去更新自身的配置。

设计思想:
1)利用消息总线触发一个客户端/bus/refresh,而划新所有客户端的配置
2)利用消息总线触发一个服务端ConfigServer的/bus/refresh端点，而刷新所有客户端的配置
具体使用：再创建一个cloud-config-client-3366和3355一模一样
加入bus使用
在服务端3344里面 加入依赖
```xml
 <!--添加消息总线RabbitMQ支持-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
```
加入配置
```yml
spring:
  #添加rabbitMQ配置
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
management:
  endpoints:
    web:
      exposure:
        include: "bus-refresh"

```
在客户端3355 3366里面加入依赖
````xml
 <!--添加消息总线RabbitMQ支持-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
````
加入配置：
```yml
spring:
  #添加rabbitMQ配置
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```
测试：依次启动7001 3344 3355 3366 
浏览器输入
http://localhost:3344/master/config-dev.yml
http://localhost:3355/getname
http://localhost:3366/getname
都可以取到值
修改github上的配置文件，刷新3344有效刷新数据，3355 3366没有刷新
执行http://localhost:3344/actuator/bus-refresh的post请求后，3355 3366全部刷新
一句话：—次修改，广播通知，处处生效
定点刷新，例如只刷新3355 不刷新3366
   http://服务注册中心ip:端口/actuator/bus-refresh/服务名:端口即可
   http://localhost:3344/actuator/bus-refresh/cloud-config-client:3355
##消息驱动
###SpringCloudStream
什么是SpringCloudStream
官方定义Spring Cloud Stream是一个构建消息驱动微服务的框架。
应用程序通过inguts或者 outputs来与Spring Cloud Stream中binder对象交互。
通过我们配置来binding(绑定)，而Spring Cloud Stream的 binder对象负责与消息中间件交互。所以，我们只需要搞清楚如何与Spring Cloud Stream交互就可以方便使用消息驱动的方式。
通过使用Spring Integration来连接消息代理中间件以实现消息事件驱动。
Spring Cloud Stream为一些供应商的消息中间件产品提供了个性化的自动化配置实现,
引用了发布-订阅、消费组、分区的三个核心概念。
目前仅支持RabbitMQ、Kafka。
一句话：屏蔽底层消息中间件的差异,降低切换成本，统一消息的编程模型
***为什么可以屏蔽差异***
在没有绑定器这个概念的情况下，我们的SpringBoot应用要直接与消息中间件进行信息交互的时候，由于各消息中间件构建的初衷不同，它们的实现细节上会有较大的差异性
通过定义绑定器作为中间层，完美地实现了应用程序与消息中间件细节之间的隔离。
通过向应用程序暴露统一的Channel通道，使得应用程序不需要再考虑各种不同的消息中间件实现。
***通过定义绑定器Binder作为中间层，实现了应用程序与消息中间件细节之间的隔离。***
  Binder（类似于JDBC）
    INPUT对应于消费者
    OUTPUT对应于生产者 

Middleware：  中间件，目前只支持RabbitMQ和Kafka
Binder： Binder是应用与消息中间件之间的封装，目前实行了Kafka和RabbitMQ的Binder，
通过  Binder可以很方便的连接中间件，可以动态的改变消息类型(对应于Kafka的topic,RabbitMQ的exchange)，这些都可以通过配置文件来实现             |
@Input： 注解标识输入通道，通过该输入通道接收到的消息进入应用程序                
@Output： 注解标识输出通道，发布的消息将通过该通道离开应用程序  
@StreamListener： 监听队列，用于消费者的队列的消息接收         
@EnableBinding： 指信道channel和exchange绑定在一起                   |
###入门：
建立cloud-stream-rabbitmq-provider8801提供者
导入依赖：
```xml
    <dependencies>
        <!--引入stream-rabbit-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
        </dependency>

        <!--引入eureka-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>
```
写配置：
```yaml
server:
  port: 8801
spring:
  application:
    name: cloud-stream-provider
  cloud:
    stream:
      binders: #在此处配置要绑定的rabbitmq的服务信息;
        defaultRabbit: #表示定义的名称，用于于binding整合
          type: rabbit #消息组件类型
          environment: #设置rabbitmq的相关的环境配置
             spring:
               rabbitmq:
                 host: localhost
                 port: 5672
                 username: guest
                 password: guest
      bindings: #服务的整合处理
        output: #这个名字是一个通道的名称
          destination: studyExchange #表示要使用的Exchange名称定义
          content-type: application/json #设置消息类型，本次为json，文本则设置"text/plain"
          binder: defaultRabbit #设置要绑定的消息服务的具体设置

eureka:
  client:
    register-with-eureka: true #false表示自己是服务中心，不需要注册自己
    service-url:
    #设置与Eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka7001.com:7001/eureka #单机版
      #集群版本
      #defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
    fetch-registry: false
  instance:
    instance-id: payment8001 #配置这个就会在eureka上显示该名称
    prefer-ip-address: true #配置了这个在eureka上鼠标放上去就会显示IP
    #lease-expiration-duration-in-seconds: 2  #服务端接受最后一次发送心跳等待时间，超时剔除 默认90秒
    #lease-renewal-interval-in-seconds: 1 #客户端向服务端发送请求时间间隔 默认30秒

```
写业务类：
```java
public interface ImessageProvider {
    public String send();
}

```
包不要导错了
```java
package com.cyz.service.impl;

import com.cyz.service.ImessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author cyz
 * @date 2021/7/31 0031 15:38
 */
@EnableBinding(Source.class)//定义消息的推送管道
public class ImessageProviderImpl implements ImessageProvider {

    @Resource
    private MessageChannel output;//消息发送管道

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build());
        System.out.println("serial:"+serial);
        return null;
    }
}
```
controller：
```java
@RestController
public class SendMessageController {

    @Resource
    private ImessageProvider imessageProvider;
    @GetMapping("/sendMessage")
    public String sendMessage(){
        return imessageProvider.send();
    }
}

```
启动类
```java
@SpringBootApplication
public class StreamApplication8801 {
    public static void main(String[] args) {
        SpringApplication.run(StreamApplication8801.class,args);
    }
}
```
测试：启动7001 rabbitmq  8801 访问http://localhost:8801/sendMessage 控制台有输出即提供者创建成功
建立cloud-stream-rabbitmq-consumer8802消费者
导入依赖：
```xml
    <dependencies>
        <!--引入stream-rabbit-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
        </dependency>

        <!--引入eureka-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>
```
写配置：区别就在output和input
```yaml
server:
  port: 8801
spring:
  application:
    name: cloud-stream-provider
  cloud:
    stream:
      binders: #在此处配置要绑定的rabbitmq的服务信息;
        defaultRabbit: #表示定义的名称，用于于binding整合
          type: rabbit #消息组件类型
          environment: #设置rabbitmq的相关的环境配置
             spring:
               rabbitmq:
                 host: localhost
                 port: 5672
                 username: guest
                 password: guest
      bindings: #服务的整合处理
        input: #这个名字是一个通道的名称
          destination: studyExchange #表示要使用的Exchange名称定义
          content-type: application/json #设置消息类型，本次为json，文本则设置"text/plain"
          binder: defaultRabbit #设置要绑定的消息服务的具体设置

eureka:
  client:
    register-with-eureka: true #false表示自己是服务中心，不需要注册自己
    service-url:
    #设置与Eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka7001.com:7001/eureka #单机版
      #集群版本
      #defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
    fetch-registry: false
  instance:
    instance-id: payment8001 #配置这个就会在eureka上显示该名称
    prefer-ip-address: true #配置了这个在eureka上鼠标放上去就会显示IP
    #lease-expiration-duration-in-seconds: 2  #服务端接受最后一次发送心跳等待时间，超时剔除 默认90秒
    #lease-renewal-interval-in-seconds: 1 #客户端向服务端发送请求时间间隔 默认30秒

```
controller：
```java
package com.cyz.controller;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author cyz
 * @date 2021/7/31 0031 16:11
 */
@Component
@EnableBinding(Sink.class)//标注为接受
public class ReceiveMessageListenerController {

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message){
        System.out.println("消息："+message.getPayload()+8802);
    }
}
```
启动类
```java
@SpringBootApplication
public class StreamConsumer8802 {
    public static void main(String[] args) {
        SpringApplication.run(StreamConsumer8802.class,args);
    }
}
```
测试启动 7001 rabbitmq 8801 8802 访问http://localhost:8801/sendMessage 8802和8801控制台有输出一样即可
创建一个8803消费者和8802一模一样
目前是8802/8803同时都收到了，存在重复消费问题
  比如在如下场景中，订单系统我们做集群部署，都会从RabbitMQ中获取订单信息，
  那如果一个订单同时被两个服务获取到，那么就会造成数据错误，我们得避免这种情况。
  这时我们就可以使用Stream中的消息分组来解决
    
  注意在Stream中处于同一个group中的多个消费者是竞争关系，就能够保证消息只会被其中一个应用消费一次。
  不同组是可以全面消费的(重复消费)，同一组内会发生竞争关系，只有其中一个可以消费。
stream会默认分组，每个都分一次，所有存在重复消费
原理：微服务应用放置于同一个group中，就能够保证消息只会被其中一个应用消费一次。不同的组是可以消费的，同一个组内会发生竞争关系，只有其中一个可以消费。
***自定义组：***
  8802/8803都变成不同组，group两个不同
  在8802 8803 下面分别写创建组cyzA,cyzB
```yaml
bindings: #服务的整合处理
        input: #这个名字是一个通道的名称
          destination: studyExchange #表示要使用的Exchange名称定义
          content-type: application/json #设置消息类型，本次为json，文本则设置"text/plain"
          binder: defaultRabbit #设置要绑定的消息服务的具体设置
          group: cyzA 用于自定义分组
```
  8802/8803实现了轮询分组，每次只有一个消费者
  8801模块的发的消息只能被8802或8803其中一个接收到，这样避免了重复消费。
  在8802 8803 下面分别写创建相同组cyzA
***持久化***
把8802中的group去掉，停掉8802 8803 发送请求，启动8802 8803 ，发现8802没有消费未消费的信息 8803启动后就消费
##分布式请求链式跟踪
spring cloud sleuth
为什么有它：在微服务框架中，一个由客户端发起的请求在后端系统中会经过多个不同的的服务节点调用来协同产生最后的请求结果，每一个前段请求都会形成一条复杂的分布式服务调用链路，链路中的任何一环出现高延时或错误都会引起整个请求最后的失败。

Spring Cloud Sleuth提供了一套完整的服务跟踪的解决方案在分布式系统中提供追踪解决方案并且兼容支持了zipkin

下载zipkin-server.jar :https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec

执行java -jar zipkin-server-2.12.9-exec.jar  后 http://localhost:9411/zipkin/
使用：使用原先的consumer80和provider8001
在两个里面添加依赖
```XML
   <!--引入zipkin-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>
```
加配置：
```yaml
spring:
  application:
    name: cloud-payment-consumer
  zipkin:
    base-url: http://localhost:9411
    sleuth:
      sampler:
        #采样率值介于0到1之间 1表示100%
        probability: 1
```
测试 启动7001 zipkin 8001 80 多访问几次，打开zipkin界面，可以看懂啊调用信息
#spring cloud Alibaba
