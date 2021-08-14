package com.cyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cyz
 * @date 2021/8/14 0014 15:30
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application9003 {
    public static void main(String[] args) {
        SpringApplication.run(Application9003.class,args);
    }
}
