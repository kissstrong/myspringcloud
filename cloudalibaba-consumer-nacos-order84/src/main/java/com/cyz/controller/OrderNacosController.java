package com.cyz.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author cyz
 * @date 2021/8/8 0008 13:58
 */
@RestController
public class OrderNacosController {
    @Autowired
    private RestTemplate restTemplate;
    private String RestUrl = "http://nacos-payment-provider";
    @GetMapping("/get")
    //@SentinelResource(value = "get")//什么都没有配置
    //@SentinelResource(value = "get",fallback = "deal_fallback")//配置了fallback
    //@SentinelResource(value = "get",blockHandler = "deal_blockHandler")//配置了blockHandler
    //两个都配置的时候但错误情况下又限流报错则返回限流
    @SentinelResource(value = "get",blockHandler = "deal_blockHandler",fallback = "deal_fallback")//配置了fallback和blockHandler
    public String get(String id){
        if (id.equals("1")){
            throw   new RuntimeException("参数错误");
        }
        return restTemplate.getForObject(RestUrl+"/get",String.class);
    }

    public String deal_fallback(String id){
        return "我是报错的兜底方法,id="+id;
    }
    public  String deal_blockHandler(String id,BlockException e){
        return "我是流量监控兜底方法";
    }

}
