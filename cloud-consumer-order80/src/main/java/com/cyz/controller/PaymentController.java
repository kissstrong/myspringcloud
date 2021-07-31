package com.cyz.controller;


import com.cyz.bean.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    //单点调用
    //public static final String PAYMENTPATH="http://localhost:8001/";
    /*轮询调用*/
    public static final String PAYMENTPATH="http://CLOUD-PAYMENT-SERVICE";
    @ApiOperation(value="插入数据", notes="插入数据")
    @PostMapping("payment/create")
    public CommonResult create(String serial){
        CommonResult commonResult = restTemplate.postForObject(PAYMENTPATH + "/provider/payment/create", serial, CommonResult.class);
        return commonResult;
    }
    @ApiOperation(value="查询数据", notes="查询数据")
    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id){

        CommonResult commonResult = restTemplate.getForObject(PAYMENTPATH + "/provider/payment/get/"+ id, CommonResult.class);
        return commonResult;
    }

    @ApiOperation(value="查询testzipkin数据", notes="查询testzipkin数据")
    @GetMapping("/testzipkin")
    public String testzipkin(){
        return restTemplate.getForObject(PAYMENTPATH + "/testzipkin/", String.class);
    }
}
