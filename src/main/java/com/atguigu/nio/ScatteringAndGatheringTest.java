package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.BatchUpdateException;
import java.util.Arrays;

/**
 * Scattering: 将数据写入到Buffer时，可以采用 buffer数组，依次写入[分散]
 * Gathering ： 从 buffer 读取数据，可以采用 buffer 数组，依次读
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {
        //使用 ServerSocketChannel 和 socketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7777);

        //绑定端口到 socket ，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建 buffer 数组
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;  //假定从客户端接收8个字节

        //循环的读取
        while (true) {
            int byteRed = 0;

            while (byteRed < messageLength) {
                socketChannel.read(buffers);
                byteRed += 1;
                System.out.println("byteRed=" + byteRed);

                //使用流打印，看看当前的 buffer 和 position 和 limit
                Arrays.asList(buffers).stream().map(buffer -> "postion" + buffer.position() +
                        ",limit=" + buffer.limit()).forEach(System.out::println);
            }

            //将所有的buffer 反转
            Arrays.asList(buffers).forEach(b -> b.flip());
        }


    }
}
