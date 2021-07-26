package com.cyz.controller;


import com.cyz.service.PaymentFeignService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyz
 * @date 2021/7/18 0018 14:08
 */
@Api(description = "支付信息管理" ,basePath = "payment")
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "global_fallBack_Method")
public class PaymentController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @ApiOperation(value="查询数据", notes="查询数据")
    @GetMapping("feign/payment/ok")
    public String getPayment(){

        String ok = paymentFeignService.ok();
        return ok;
    }

    @GetMapping("feign/payment/error")
    @HystrixCommand(fallbackMethod = "error_handler",commandProperties = {//设置超时时间，超时就调用兜底方法
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
    public String error(){
       return paymentFeignService.error();
    }
    public String error_handler(){
        return "我是兜底方法80，上面的方法超时或出错";
    }
    @GetMapping("feign/payment/global")
    @HystrixCommand(commandProperties = {//设置超时时间，超时就调用兜底方法
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
    public String TestGlobal(){
        int i =10/0;

        return "ok";
    }
    public String global_fallBack_Method(){
        return "我是全局回调方法";
    }
}
