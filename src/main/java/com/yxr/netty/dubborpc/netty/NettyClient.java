package com.yxr.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    //创建线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler clientHandler;

    //编写方法使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass,final String provideName){
        /*
            public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          InvocationHandler h)
         */
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {serviceClass},
                (proxy,method,args)->{//lambda表达式
                    //客户端每调用一次hello就会进入到该代码
                    System.out.println("代理工具人在工作...");
                    if(clientHandler == null){
                        initClient();
                    }
                    //provideName协议头
                    //agrs 客户端传送的参数hello(args)
                    clientHandler.setPara(provideName + args[0]);
                    //executorService.submit()方法需要传入一个callable接口
                    //NettyClientHandler clientHandler已经继承了callable接口了
                    //传入后会自行调用call方法
                    //获取返回值（callable是有返回值的）
                    return executorService.submit(clientHandler).get();
                });
    }


    //初始化客户端
    private static void initClient(){
        clientHandler = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true) //没有延时
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(clientHandler);
                    }
                });
        try {
            bootstrap.connect("127.0.0.1",8081).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
