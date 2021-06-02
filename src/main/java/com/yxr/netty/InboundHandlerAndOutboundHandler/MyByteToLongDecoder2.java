package com.yxr.netty.InboundHandlerAndOutboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyByteToLongDecoder2 extends ReplayingDecoder<Void>{
    //void不需要自己处理
    //并不是所有Bytebuf都支持
    //和ByteToMessageDecoder 比较慢
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder2被调用...");
        out.add(in.readLong());
    }
}
