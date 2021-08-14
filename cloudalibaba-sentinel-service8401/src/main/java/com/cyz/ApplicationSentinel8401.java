package com.cyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cyz
 * @date 2021/8/14 0014 12:54
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ApplicationSentinel8401 {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationSentinel8401.class,args);
    }
}
