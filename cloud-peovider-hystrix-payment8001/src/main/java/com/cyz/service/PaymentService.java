package com.cyz.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author cyz
 * @date 2021/7/25 0025 13:35
 */
@Service
public class PaymentService {
  //服务降级
    public String ok(){
        return "好的";
    }

    @HystrixCommand(fallbackMethod = "error_handler",commandProperties = {//设置超时时间，超时就调用兜底方法
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String error(){
        //int a = 10/0;
        try {
            TimeUnit.SECONDS.sleep(5);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            return "不好的";
        }
    }
    public String error_handler(){
        return "我是兜底方法，上面的方法超时或出错";
    }

    //服务熔断  所有能配置的都在HystrixCommandProperties里面
    @HystrixCommand(fallbackMethod = "testRonDuang_fallBack" , commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启熔断器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//最近10秒
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//失败率达到多少跳闸
            })
    public String testRonDuang(int id){
        if (id<0){
            throw new RuntimeException("id不能小于0");
        }
        return Thread.currentThread().getName()+"It"+"调用成功，流水号:";
    }
    public String testRonDuang_fallBack(int id){
        return "熔断回调id不能为负数";
    }
}
