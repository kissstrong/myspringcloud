package com.cyz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author cyz
 * @date 2021/7/18 0018 15:27
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
