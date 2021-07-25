package com.cyz;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author cyz
 * @date 2021/7/18 0018 13:33
 */
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients
@EnableHystrix//开启降级配置
public class ApplicationFeignHystrix80 {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationFeignHystrix80.class,args);
    }
}
