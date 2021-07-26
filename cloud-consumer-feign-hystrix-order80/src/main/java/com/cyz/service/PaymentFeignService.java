package com.cyz.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author cyz
 * @date 2021/7/25 0025 12:06
 */
@Component
@FeignClient(value = "CLOUD-HYSTRIX-PAYMENT-SERVICE",fallback = PaymentFeignServiceImpl.class)
public interface PaymentFeignService {
    @GetMapping("/ok")
    public String ok();

    @GetMapping("/error")
    public String error();
}
