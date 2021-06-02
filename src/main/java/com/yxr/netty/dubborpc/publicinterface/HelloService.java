package com.yxr.netty.dubborpc.publicinterface;

//公共接口，服务方和消费方都需要的
public interface HelloService {
    String hello(String msg);
}
