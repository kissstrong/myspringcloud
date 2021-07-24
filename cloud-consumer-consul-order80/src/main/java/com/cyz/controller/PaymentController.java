package com.cyz.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author cyz
 * @date 2021/7/18 0018 14:08
 */
@Api(description = "支付信息管理" ,basePath = "payment")
@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private RestTemplate restTemplate;

    String PATH = "http://consul-provider-payment";

    @ApiOperation(value="测试consul", notes="测试consul")
    @PostMapping("/consumer/payment/consul")
    public Object create(){
        String s = restTemplate.postForObject(PATH + "/provider/payment/consul", null, String.class);
        return "consumer:"+s;
    }

}
