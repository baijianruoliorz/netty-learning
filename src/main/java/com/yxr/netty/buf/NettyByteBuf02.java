package com.yxr.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf02 {
    public static void main(String[] args){
        //读写时都需要指定编码
        ByteBuf buf = Unpooled.copiedBuffer("hello world ...", CharsetUtil.UTF_8);
    }
}
