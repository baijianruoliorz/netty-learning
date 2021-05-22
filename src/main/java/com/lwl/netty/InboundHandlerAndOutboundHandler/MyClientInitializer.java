package com.lwl.netty.InboundHandlerAndOutboundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class  MyClientInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //DefaultChannelPipeline$HeadContext#0 -》MyClientInitializer ->MyLongToByteEncoder->MyClientHander
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个出栈handler，对数据进行编码encoder，再加入一个自定义handler处理业务逻辑
        //出栈时先走自己的业务handler，最后才进行编码
        pipeline.addLast(new MyLongToByteEncoder())//编码encoder，出站
                .addLast(new MyByteToLongDecoder())//解码decoder，入站
                .addLast(new MyClientHandler());
    }
}
