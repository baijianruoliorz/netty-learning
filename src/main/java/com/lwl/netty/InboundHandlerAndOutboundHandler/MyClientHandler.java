package com.lwl.netty.InboundHandlerAndOutboundHandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("从服务器（"+ctx.channel().remoteAddress()+"）得到的数据：" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler发送数据...");
        //这里的write会向服务端发数据（相当于出栈？在入栈的handler里出栈？）
        //发一次
        ctx.writeAndFlush(123567L);
        //16个字节，会分两次发
        //MyLongToByteEncoder 的父类MessageToByteEncoder 会判断是否需要调用MyLongToByteEncoder的encode方法
        //感觉MyLongToByteEncoder中的泛型判断
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdergdaaaabbbb", CharsetUtil.UTF_8));
    }
}
