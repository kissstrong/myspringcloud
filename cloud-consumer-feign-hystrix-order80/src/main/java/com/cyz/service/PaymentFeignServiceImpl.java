package com.cyz.service;

import org.springframework.stereotype.Component;

/**
 * @author cyz
 * @date 2021/7/25 0025 15:47
 */
@Component
public class PaymentFeignServiceImpl implements PaymentFeignService {
    @Override
    public String ok() {
        return "我是OK的回调方法";
    }

    @Override
    public String error() {
        return "我是Error的回调方法";
    }
}
