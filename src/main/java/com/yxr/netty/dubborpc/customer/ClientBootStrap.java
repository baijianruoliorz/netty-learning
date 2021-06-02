package com.yxr.netty.dubborpc.customer;

import com.yxr.netty.dubborpc.netty.NettyClient;
import com.yxr.netty.dubborpc.publicinterface.HelloService;

public class ClientBootStrap {
    //定义协议头
    public static String providerName = "helloService#hello#";
    public static void main(String[] args) throws InterruptedException {
        //创建一个消费者
        NettyClient customer = new NettyClient();
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);
        //通过代理对象调用服务方提供的方法
        for (;;){
            Thread.sleep(1*1000);
            String res = service.hello("nihao");
            System.out.println("服务器返回的结果 ： " + res);
        }
    }
}
