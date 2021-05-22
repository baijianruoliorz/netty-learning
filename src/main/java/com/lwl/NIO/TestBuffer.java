package com.lwl.NIO;

import java.nio.IntBuffer;

public class TestBuffer {
    public static void main(String[] args){
        IntBuffer intBuffer = IntBuffer.allocate(10);
        for (int i = 0; i < 10; i++) {
            intBuffer.put(i);
        }
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
