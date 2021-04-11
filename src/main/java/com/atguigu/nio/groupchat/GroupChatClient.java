package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author jarvis
 * @date 2021/4/11 0011 20:21
 */
public class GroupChatClient {

    //定义相关的属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;

    private SocketChannel socketChannel;

    private String userName;


    //构造器，初始化工作
    public GroupChatClient() {
        try {
            selector = Selector.open();

            //连接服务器
            socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));

            //设置非阻塞
            socketChannel.configureBlocking(false);

            //将 channel 注册到 selector
            socketChannel.register(selector, SelectionKey.OP_READ);

            //得到 username
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok....");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //向服务器发送消息
    public void sendMsg(String info) {
        info = userName + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务端回复的消息
    public void readInfo() {
        try {

            int readChannel = selector.select();
            if (readChannel > 0) { //有可用的通道
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {

                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到一个 Buffer
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        //读取
                        sc.read(byteBuffer);

                        //把读取到缓冲区的数据转成字符串
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());

                    }
                    //这一步很重要！！
                    //这一步很重要！！
                    //这一步很重要！！
                    keyIterator.remove();


                }

            } else {
                // System.out.println("没有可用的通道.....");
            }


        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        //启动我们的客户端

        GroupChatClient chatClient = new GroupChatClient();

        //启动一个线程,每隔3秒，读取从服务器发送的数据
        new Thread() {
            @Override
            public void run() {
                while (true) {

                    chatClient.readInfo();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            }
        }.start();

        //发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.nextLine();
            chatClient.sendMsg(str);
        }

    }

}
