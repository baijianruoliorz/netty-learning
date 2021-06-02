package com.yxr.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/*
方法被调用顺序 1）channelActive->2)setPara->3)call->4)channelRead->5)call
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable{
    private ChannelHandlerContext context;//上下文
    private String result;  //返回的结果
    private String para;    //客户端调用方法时传入的参数
    //第一个被调用的方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //与服务器连接创建成功后调用的方法
        System.out.println("channelActive被调用...");
        context = ctx;//其他地方要用到
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //收到服务器的数据时被调用的方法
        result = msg.toString();
//        System.out.println(result);
        //唤醒等待的线程
        notify();
    }

    @Override
    public  void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //被代理对象调用，发生数据给服务器，发送完就等待，直到被(channelRead)唤醒,在返回结果
    @Override
    public synchronized Object call() throws Exception {
        //这就是server端接受到的msg
        context.writeAndFlush(para);
        //结果还没返回，waiting
//        System.out.println("call : " + result);
        wait();
        //醒来时已经获得了服务器返回的数据，被channelRead存放在了result中
//        System.out.println("call : " + result);
        return result;
    }

    public void setPara(String para) {
        this.para = para;
    }
}
