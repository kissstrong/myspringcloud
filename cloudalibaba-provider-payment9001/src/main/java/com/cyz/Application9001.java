package com.cyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cyz
 * @date 2021/8/8 0008 13:03
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application9001 {
    public static void main(String[] args) {
        SpringApplication.run(Application9001.class,args);
    }
}
