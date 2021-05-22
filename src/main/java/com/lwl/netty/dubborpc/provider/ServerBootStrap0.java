package com.lwl.netty.dubborpc.provider;

import com.lwl.netty.dubborpc.netty.NettyServer;

//启动服务提供者nettyServer
public class ServerBootStrap0 {
    public static void main(String[] args){
        NettyServer.startServer("127.0.0.1",8081);
    }
}
