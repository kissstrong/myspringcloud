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
public class Application3377 {
    public static void main(String[] args) {
        SpringApplication.run(Application3377.class,args);
    }
}
