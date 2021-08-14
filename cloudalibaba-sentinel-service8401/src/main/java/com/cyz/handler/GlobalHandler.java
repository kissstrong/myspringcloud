package com.cyz.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author cyz
 * @date 2021/8/14 0014 15:11
 */
public class GlobalHandler {
//一定要使用static修饰，不然访问不到
    public static String handler1(BlockException e){
        return  "handler1";
    }

    public static String handler2(BlockException e){
        return  "handler2";
    }
}
