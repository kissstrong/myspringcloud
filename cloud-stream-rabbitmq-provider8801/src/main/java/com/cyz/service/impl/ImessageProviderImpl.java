package com.cyz.service.impl;

import com.cyz.service.ImessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author cyz
 * @date 2021/7/31 0031 15:38
 */
@EnableBinding(Source.class)//定义消息的推送管道
public class ImessageProviderImpl implements ImessageProvider {

    @Resource
    private MessageChannel output;//消息发送管道

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build());
        System.out.println("serial:"+serial);
        return null;
    }
}
