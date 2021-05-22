package com.lwl.netty.InboundHandlerAndOutboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder{
    /**
     *
     * @param ctx   上下文对象
     * @param in    入栈的bytebuf
     * @param out   list集合，解码后的数据传给下一个handler（inbound）
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //因为Long是八个字节，小于八个读取不了
        //只要管道中还有数据就会一直调用MyByteToLongDecoder，所以使用if
        System.out.println("MyByteToLongDecoder的decode方法...");
        if(in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
