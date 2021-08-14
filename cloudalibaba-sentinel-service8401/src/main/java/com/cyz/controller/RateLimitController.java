package com.cyz.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cyz.handler.GlobalHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyz
 * @date 2021/8/14 0014 14:59
 */
@RestController
public class RateLimitController {

    @GetMapping("/byResource")
    //value一般使用请求名有了value 资源名也可以写value  blockHandler 处理限流兜底
    @SentinelResource(value = "byResource",blockHandler = "deal_byResource")
    public String byResource(){
        return  "byResource";
    }

    public String deal_byResource(BlockException e){
        return  "deal_byResource"; // sentinel系统默认的提示: BLocked by sentinel (fLow limiting)

    }

//没有处理方法就是默认的
    @GetMapping("rateLimit/byUrl")
    //value一般使用请求名有了value
    @SentinelResource(value = "byUrl")
    public String byUrl(){
        return  "byUrl";
    }


    @GetMapping("globalHandler")
    //value一般使用请求名有了value
    @SentinelResource(value = "globalHandler",blockHandlerClass = GlobalHandler.class,blockHandler = "handler1")
    public String globalHandler(){
        return  "globalHandler";
    }


}
