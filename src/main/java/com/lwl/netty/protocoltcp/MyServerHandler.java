package com.lwl.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol>{
    private int count=0;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务器接收到的信息：" );
        System.out.println("长度: " + len);
        System.out.println("内容: " + new String(content,CharsetUtil.UTF_8));
        System.out.println("接收次数：" + (++count));
        System.out.println("------------------------------------------------------");
        //接收到消息后回复
        String responsemsg = UUID.randomUUID().toString();
        byte[] responsemsg2 = responsemsg.getBytes("utf-8");
//        int len2 = responsemsg2.length;
        int len2 = responsemsg.getBytes("utf-8").length;

        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(responsemsg2);
        messageProtocol.setLen(len2);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
