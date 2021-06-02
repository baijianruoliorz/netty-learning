package com.yxr.netty.dubborpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {
    //避免方法直接暴露
    public static void startServer(String hostname,int port){
        startServer0(hostname,port);
    }

    //对nettyserver初始化和启动
    private static void startServer0(String hostname,int port){
        EventLoopGroup bossgroup = new NioEventLoopGroup();
        EventLoopGroup workergroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossgroup,workergroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(hostname, port).sync();
            System.out.println("服务方开启...");
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        } finally{
            bossgroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }

    }
}
