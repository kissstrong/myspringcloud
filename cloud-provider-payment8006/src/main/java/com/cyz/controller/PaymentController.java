package com.cyz.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyz
 * @date 2021/7/18 0018 14:08
 */
@Api(description = "支付信息管理" ,basePath = "payment")
@RestController
@Slf4j
public class PaymentController {



    @ApiOperation(value="测试consul", notes="测试consul")
    @PostMapping("/provider/payment/consul")
    public Object create(){

        return "8006:consul";
    }

}
