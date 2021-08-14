package com.cyz.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author cyz
 * @date 2021/8/14 0014 12:55
 */
@RestController
public class SentinelController {
    @GetMapping("/testA")
    public String testA(){
        return  "testA";
    }

    @GetMapping("/testB")
    public String testB(){
        return  "testB";
    }

    @GetMapping("/testD")
    public String testD(){
        try {//暂停几秒线程
            TimeUnit.SECONDS.sleep(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  "testD RT";
    }

    @GetMapping("/hotKey")
    //value一般使用请求名  blockHandler 处理限流兜底
    @SentinelResource(value = "hotKey",blockHandler = "deal_hotKey")
    public String hotKey(@RequestParam(value = "p1",required = false) String p1,
                         @RequestParam(value = "p2",required = false)String p2){

        return  "hotKey";
    }

    public String deal_hotKey(String p1, String p2 , BlockException e){
        return  "deal_hotKey"; // sentinel系统默认的提示: BLocked by sentinel (fLow limiting)

    }
}
