package com.cyz.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyz
 * @date 2021/8/8 0008 13:55
 */
@RestController
@RefreshScope//动态刷新
public class PaymentController {


    @Value("${student.name}")
    private String name;
    @Value("${student.age}")
    private Integer age;

    @GetMapping("/config")
    public String get(){
        return "name:"+name+",age:"+age;
    }
}
