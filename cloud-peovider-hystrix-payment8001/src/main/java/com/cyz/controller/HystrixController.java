package com.cyz.controller;

import com.cyz.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyz
 * @date 2021/7/25 0025 13:38
 */
@RestController
public class HystrixController {
    @Autowired
    private PaymentService paymentService;
    @GetMapping("/ok")
    public String ok(){
       return paymentService.ok();
    }

    @GetMapping("/error")
    public String error(){
        return paymentService.error();
    }

    @GetMapping("/testronduan")
    public String testronduan(int id){

        return paymentService.testRonDuang(id);
    }
}
