package com.cyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cyz
 * @date 2021/8/14 0014 15:38
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application84 {
    public static void main(String[] args) {
        SpringApplication.run(Application84.class,args);
    }
}
