package com.cyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author cyz
 * @date 2021/7/26 0026 18:57
 */
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashBoard9001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashBoard9001.class,args);
    }
}
