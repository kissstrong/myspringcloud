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
##openFeign
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

####新建服务模块cloud-provider-hystrix-payment8001
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


#spring cloud Alibaba
