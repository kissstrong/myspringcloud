package com.cyz;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @author cyz
 * @date 2021/7/18 0018 13:33
 */
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients
public class ApplicationFeign80 {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationFeign80.class,args);
    }
}
