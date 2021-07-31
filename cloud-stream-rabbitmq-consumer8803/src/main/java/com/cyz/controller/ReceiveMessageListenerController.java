package com.cyz.controller;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author cyz
 * @date 2021/7/31 0031 16:11
 */
@Component
@EnableBinding(Sink.class)//标注为接受
public class ReceiveMessageListenerController {

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message){
        System.out.println("消息："+message.getPayload()+8803);
    }
}
