package com.cyz.service;

import com.cyz.bean.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author cyz
 * @date 2021/7/25 0025 12:06
 */
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {
    @GetMapping("/provider/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id);

    @GetMapping("/provider/payment/feign")
    public String getPaymentById();
}
