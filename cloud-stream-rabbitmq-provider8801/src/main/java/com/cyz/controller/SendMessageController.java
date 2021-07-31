package com.cyz.controller;

import com.cyz.service.ImessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cyz
 * @date 2021/7/31 0031 15:52
 */
@RestController
public class SendMessageController {

    @Resource
    private ImessageProvider imessageProvider;
    @GetMapping("/sendMessage")
    public String sendMessage(){
        return imessageProvider.send();
    }
}
