package com.cyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author cyz
 * @date 2021/7/18 0018 13:33
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class Application8002 {
    public static void main(String[] args) {
        SpringApplication.run(Application8002.class,args);
    }
}
