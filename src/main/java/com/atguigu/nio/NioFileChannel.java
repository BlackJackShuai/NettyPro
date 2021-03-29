package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author jarvis
 * @date 2021/3/29 0029 21:22
 */
public class NioFileChannel {

    public static void main(String[] args) throws IOException {
        String str = "Hello ,尚硅谷";

        //创建一个输出流 -> channel
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\file.txt");

        //通过 fileOutputStream 获取对应的 FileChannel
        //这个 channel 真实的类型是 FileChannelImpl
        FileChannel channel = fileOutputStream.getChannel();

        //创建一个缓存区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将 str 放到 byteBuffer
        byteBuffer.put(str.getBytes());

        //对 byteBuffer 进行反转
        byteBuffer.flip();

        //将 byteBuffer 数据写到 fileChannel
        channel.write(byteBuffer);
        fileOutputStream.close();


    }
}
