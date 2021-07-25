package com.cyz.controller;


import com.cyz.bean.CommonResult;
import com.cyz.service.PaymentFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyz
 * @date 2021/7/18 0018 14:08
 */
@Api(description = "支付信息管理" ,basePath = "payment")
@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @ApiOperation(value="查询数据", notes="查询数据")
    @GetMapping("feign/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id){

        CommonResult paymentById = paymentFeignService.getPaymentById(id);
        return paymentById;
    }

    @GetMapping("feign/payment/feign")
    public String getPaymentById(){
       return paymentFeignService.getPaymentById();
    }
}
