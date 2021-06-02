package com.yxr.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args){

        //buf -> byte[10]           unlooped -》 非池化
        ByteBuf buf = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            //如果整数没有超过byte就会自己转换
            buf.writeByte(i);
        }
        //在这个buf中不需要flip进行反转（底层维护了一个readerIndex 和 writerIndex）
        for (int i = 0; i < buf.capacity(); i++) {
            System.out.println(buf.readByte());
        }
//        for (int i = 0; i < buf.capacity(); i++) {
//            System.out.println(buf.readByte());
//        }
        buf.readerIndex();
    }
}
