package com.yxr.NIO;


import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ChannelTest {
    public static void main(String[] args) throws Exception {
//        writeFile();
//        readFile();
//        ChannelToChannel();
//        ChannelTransfer();
//        readonlyBuffer();
//        mapBuffer();
        ScatterAndGather_SocketChannel();


    }

    private static void ScatterAndGather_SocketChannel() throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.socket().bind(new InetSocketAddress(7000));
        //分配客户端 一个 socket
        SocketChannel sChannel = ssChannel.accept();
        try {
            ByteBuffer[] buffers = new ByteBuffer[2];
            buffers[0] = ByteBuffer.allocate(10);
            buffers[1] = ByteBuffer.allocate(5);
            int len = 15;
            while (true){
                int byteRead = 0;
                while (byteRead < len){
                    long readnum = sChannel.read(buffers);
                    byteRead += readnum;
                    System.out.println("byteread : " + byteRead);
                    //使用流打印
                    Arrays.asList(buffers).stream().map(buffer -> "positon : " + buffer.position() + ", limit :" + buffer.limit()).forEach(System.out::println);
                }
                //将数组中的buffer都flip
                Arrays.asList(buffers).forEach(buffer -> buffer.flip());
                int writeByte = 0;
                //将数据显示会客户端
                while (writeByte < len){
                    long writenum = sChannel.write(buffers);
                    writeByte += writenum;
                }
                Arrays.asList(buffers).forEach(buffer -> buffer.clear());
                System.out.println("byteRead = " + byteRead + ", byteWrite = " + writeByte + ", messagelength = " + len);
            }
        }finally {
            ssChannel.close();
            sChannel.close();
        }

    }
    //直接操作内存的数据，无需复制到虚拟机堆内存中
    private static void mapBuffer() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 50);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(1, (byte) 'H');
        mappedByteBuffer.put(2, (byte) 'H');
        mappedByteBuffer.put(10, (byte) '9');
//        mappedByteBuffer.put(5, (byte) 'Y');//IndexOutOfBoundsException
        randomAccessFile.close();
        System.out.println("修改成功~~");
    }

    private static void readonlyBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        for (int i = 0; i < 10; i++) {
            buffer.put((byte) (i*3));
        }
        buffer.flip();
        ByteBuffer readonlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readonlyBuffer.getClass());
        while (readonlyBuffer.hasRemaining()){
            System.out.println(readonlyBuffer.get());
        }
        readonlyBuffer.put((byte)1);//ReadOnlyBufferException
    }

    private static void ChannelTransfer() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\128621\\777.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\128621\\2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        fileChannel01.transferTo(0,fileChannel01.size(),fileChannel02);

        fileChannel01.close();
        fileChannel02.close();
        fileInputStream.close();
        fileOutputStream.close();
    }

    private static void ChannelToChannel() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\128621\\777.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (fileChannel01.read(byteBuffer) != -1){
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
            byteBuffer.clear();
        }
        fileChannel01.close();
        fileChannel02.close();
    }

    private static void readFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("D:\\128621\\777.txt"));
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        System.out.println(new String(buffer.array()));
//        int len = 0;
//        while ((len=channel.read(buffer)) != -1){
//            buffer.flip();
//            System.out.println(new String(buffer.array(),0,len));
//            buffer.clear();
//        }
        fileInputStream.close();
    }

    private static void writeFile() throws IOException {
        String str = "fuck U!";
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\128621\\777.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put(str.getBytes());
        buffer.flip();
        channel.write(buffer);

        fileOutputStream.close();
    }
}
