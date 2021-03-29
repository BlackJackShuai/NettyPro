package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 读取文件内容，经过 channel ，然后输出
 *
 * @author jarvis
 * @date 2021/3/29 0029 21:54
 */
public class NioFileChannel02 {

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\file.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过 fileInputStream 获取对应的 channel  -> 实际类型 FileChannelImpl
        FileChannel channel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道 channel 的数据读到 ByteBuffer
        channel.read(byteBuffer);

        // 将 byteBuffer 的字节数据，转换成String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }


}
