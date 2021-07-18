package com.cyz.controller;


import com.cyz.bean.CommonResult;
import com.cyz.bean.Payment;
import com.cyz.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cyz
 * @date 2021/7/18 0018 14:08
 */
@Api(description = "支付信息管理" ,basePath = "payment")
@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @ApiOperation(value="插入数据", notes="插入数据")
    @PostMapping("/provider/payment/create")
    public CommonResult create(@RequestBody String serial){
        int result = paymentService.create(serial);
        log.info("*********插入的结果："+result);
        if (result >0){
            return new CommonResult(200,"插入数据库成功",result);
        }else {
            return new CommonResult(444,"插入数据库失败",result);

        }
    }
    @ApiOperation(value="查询数据", notes="查询数据")
    @GetMapping("/provider/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id){

        Payment payment = paymentService.getPayment(id);
        log.info("*********查询的结果："+payment);
        if (payment != null){
            return new CommonResult(200,"查询的结果",payment);
        }else {
            return new CommonResult(444,"查询数据库失败");
        }
    }
}
