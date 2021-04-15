package com.atguigu.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author jarvis
 * @date 2021/4/15 0015 22:08
 */
public class NewIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("localhost", 7001));

        String fileName = "protoc-3.6-win.zip";

        //得到一个文件 Channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        //准备发送
        long start = System.currentTimeMillis();

        //在 linux 下一个 transferTo 方法就可以完成传输
        // 在 windows 下一次调用 tranferTo 只能发送 8M，就需要分段传输，
        // transferTo 底层就是用的 零拷贝
        long trunsferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总的字节数:=" + trunsferCount + "耗时：=" + (System.currentTimeMillis() - start));

        //关闭
        fileChannel.close();


    }
}
