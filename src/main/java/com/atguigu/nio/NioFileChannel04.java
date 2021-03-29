package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 只使用 channel 管道，实现两个文件之间的拷贝 （使用transferFrom 方法）
 *
 * @author jarvis
 * @date 2021/3/29 0029 22:11
 */
public class NioFileChannel04 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\a.png");

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\a_copy.png");

        //获取各个流对应的 fileChannel
        FileChannel source = fileInputStream.getChannel();
        FileChannel dest = fileOutputStream.getChannel();

        //使用 transferForm 完成两个 channel 管道间的数据的拷贝
        dest.transferFrom(source, 0, source.size());

        //关闭相关的流
        source.close();
        dest.close();

        fileInputStream.close();
        fileOutputStream.close();

    }


}
