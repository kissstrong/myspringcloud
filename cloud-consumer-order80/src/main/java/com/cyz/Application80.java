package com.cyz;

import com.myrule.MyselfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author cyz
 * @date 2021/7/18 0018 13:33
 */
@SpringBootApplication
@EnableSwagger2 //name表示访问的服务名称 configuration表示遵循的调度配置
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = MyselfRule.class)
public class Application80 {
    public static void main(String[] args) {
        SpringApplication.run(Application80.class,args);
    }
}
