package com.cyz.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cyz
 * @date 2021/7/25 0025 12:58
 */
@Configuration
public class FeignLogConfig {

    @Bean
    Logger.Level FeignLog(){
        return Logger.Level.FULL;
    }
}
