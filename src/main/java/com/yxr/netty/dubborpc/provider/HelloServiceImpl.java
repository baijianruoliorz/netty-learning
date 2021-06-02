package com.yxr.netty.dubborpc.provider;

import com.yxr.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    //当消费方调用该方法，就返回一个结果
    private int count = 0;
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息：" + msg);
        if(msg != null){
            return "hello client , 我已经收到你的消息... ["+msg+"],第" + (++count) + "次...";
        }else{
            return "hello client , 我已经收到你的消息...";
        }
    }
}
