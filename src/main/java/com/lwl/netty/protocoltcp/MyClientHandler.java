package com.lwl.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol>{
    private int count=0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送十条数据
        for (int i = 0; i < 10; i++) {
            String message = "今天天气不错啊...";
            byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
            int length = message.getBytes(CharsetUtil.UTF_8).length;
            //创建协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(bytes);
            ctx.writeAndFlush(messageProtocol);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常信息------------" + cause.getMessage());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("客户端接收到的回复消息：");
        System.out.println("长度 ： " + len);
        System.out.println("内容 ： " + new String(content,CharsetUtil.UTF_8));
        System.out.println("消息数量为 ： " + (++count));
        System.out.println("---------------------------------------");
    }
}
