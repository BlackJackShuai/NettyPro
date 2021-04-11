package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author jarvis
 * @date 2021/4/10 0010 21:33
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的 ip 和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {

            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作...");
            }


        }
        //.....如果连接成功，就发送数据
        String str = "HelloJarvis";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());//wrap:产生一个字节数组到 Buffer 里面去

        //发送数据，讲 buffer 数据写入到 channel
        socketChannel.write(buffer);
        System.in.read();

    }
}
