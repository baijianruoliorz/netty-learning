package com.yxr.NIO.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int port = 6667;
    public GroupChatServer(){
        //初始化工作
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(port));
            //NOI都要设置为非阻塞
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //监听代码
    public void listen(){
        try {
            while (true){
//                int count = selector.select(2000);//监听个两秒
                int count = selector.select();//没有发生事件会阻塞在这
                if(count > 0){//说明有事件要处理
                    //获取事件集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()){
                        SelectionKey key = keyIterator.next();
                        //判断key对应的事件
                        if(key.isAcceptable()){
                            //通道为连接事件
                            AcceptEvent();
                        }else if(key.isReadable()){
                            //通道为读事件
                            readEvent(key);
                        }
                    }
                    //把当前的事件删除,防止重复处理
                    keyIterator.remove();
                }else{
                    System.out.println("等待中...");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }
    private void AcceptEvent() throws IOException {
        SocketChannel socketChannel = listenChannel.accept();
        socketChannel.configureBlocking(false);
        //注册
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println(socketChannel.getRemoteAddress() + "上线了....");
    }
    private void readEvent(SelectionKey key) throws IOException {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            //创建缓冲
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //数据长度
            int bufferLen = socketChannel.read(buffer);
            //根据长度做处理
            if(bufferLen > 0){
                String msg = new String(buffer.array());
                //输出该消息
                System.out.println("from 客户端 " + msg);
                //向其他客户端(通道)转发消息,要排除自己
                SendMsgToOtherClient(msg,socketChannel);
            }
        }catch (IOException e){
            System.out.println(socketChannel.getRemoteAddress() + "离线了...");
            //取消注册
            key.cancel();
            //关闭通道
            socketChannel.close();
        }
    }
    private void SendMsgToOtherClient(String msg,SocketChannel self) throws IOException {
        //向其他客户端(通道)转发消息,要排除自己
        System.out.println("服务器转发消息中...");
        //遍历所以注册到selector上的socketChanne,并排除自己
        for (SelectionKey key : selector.keys()){
            //取出通道,通过key取出对应的channel
            Channel targetChannel =  key.channel();
            //排除自己,
            if(targetChannel != self && targetChannel instanceof SocketChannel){
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg蠢到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer写入通道
                dest.write(buffer);
            }
        }
    }
    public static void main(String[] args){
        //创建一个服务器对象
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
