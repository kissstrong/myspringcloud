package com.cyz.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author cyz
 * @date 2021/7/30 0030 15:45
 */
@Component
@RefreshScope
public class User {
    @Value("${config.name}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
