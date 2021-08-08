package com.cyz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyz
 * @date 2021/8/8 0008 13:55
 */
@RestController
public class PaymentController {


    @GetMapping("/get")
    public String get(){
        return "controller:9001";
    }
}
