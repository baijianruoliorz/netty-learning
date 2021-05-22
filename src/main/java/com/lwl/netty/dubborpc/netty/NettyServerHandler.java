package com.lwl.netty.dubborpc.netty;

import com.lwl.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息并调用服务
        System.out.println("client msg : " + msg);
        //客户端在调用服务器的服务时，我们需要定义一个规范
        //比如：每次发消息时都必须以某个字符串开头"helloService#hello#" 后面加参数
        if(msg.toString().startsWith("helloService#hello#")){
            String res = new HelloServiceImpl().
                    hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(res);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
