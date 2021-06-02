package com.yxr.netty.InboundHandlerAndOutboundHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long>{
    //读数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        //读取从客户端读取的数据
        System.out.println("channelRead0");
        System.out.println("从客户端读到的数据（LONG）" + ctx.channel().remoteAddress() +":"+ msg);
        //给客户端会送信息
        ctx.writeAndFlush(9999L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
