package com.cyz.controller;

import com.cyz.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyz
 * @date 2021/7/30 0030 14:35
 */
@RestController
public class TestConfigController {

    @Autowired
    private User user;

    @RequestMapping("getname")
    private String get(){
        return user.getName();
    }
}
