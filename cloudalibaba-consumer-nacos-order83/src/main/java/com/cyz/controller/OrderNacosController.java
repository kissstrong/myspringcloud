package com.cyz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author cyz
 * @date 2021/8/8 0008 13:58
 */
@RestController
public class OrderNacosController {
    @Autowired
    private RestTemplate restTemplate;
   //读取配置文件里的配置
    @Value("${service-url.nacos-user-service}")
    private String RestUrl;
    @GetMapping("/get")
    public String get(){
        return restTemplate.getForObject(RestUrl+"/get",String.class);
    }


}
