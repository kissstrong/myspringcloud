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
