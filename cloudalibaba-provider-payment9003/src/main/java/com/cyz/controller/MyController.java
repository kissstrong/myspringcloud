package com.cyz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author cyz
 * @date 2021/8/14 0014 15:32
 */
@RestController
public class MyController {
    @GetMapping("/get")
    public String test(){
        return "9003"+ UUID.randomUUID().toString();
    }
}
