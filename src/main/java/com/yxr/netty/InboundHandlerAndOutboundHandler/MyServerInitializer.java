package com.yxr.netty.InboundHandlerAndOutboundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/*
server和client 各有一条pipeline（双向链表）
socket <->(decoderhandler,encoderhandler)<—> handler1 <->handler2 <-> handler3  <-> server(client)
入站方向就是从socket到server（client）开始必须要进行解码
出站方向就是从server（client）到socket 结束必须要进行编码
socket 发数据前要解码，收数据前要编码
 */

public class MyServerInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //DefaultChannelPipeline$HeadContext#0 ->MyServerInitializer -> MyByteToLongDecoder->MyServerHandler
        ChannelPipeline pipeline = ch.pipeline();
        //入栈的handler进行解码MyByteToLongDecoder
        pipeline//.addLast(new MyByteToLongDecoder())//解码decoder，入站
                .addLast(new MyByteToLongDecoder2())
                .addLast(new MyLongToByteEncoder())//编码encoder，出站
                .addLast(new MyServerHandler());//自定义业务handler
    }
}
