package com.cyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author cyz
 * @date 2021/7/18 0018 13:33
 */
@SpringBootApplication
@EnableSwagger2
public class Application80 {
    public static void main(String[] args) {
        SpringApplication.run(Application80.class,args);
    }
}
