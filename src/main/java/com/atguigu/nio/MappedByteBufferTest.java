package com.atguigu.nio;


import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 可以让文件直接在内存（堆外内存）中修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {

        RandomAccessFile rw = new RandomAccessFile("1.txt", "rw");
        //获取对应的通道
        FileChannel channel = rw.getChannel();


        /*
          参数1： FileChannel.MapMode.READ_WRITE 使用的读写模式
          参数2： 0：可以直接修改IDE起始位置
          参数3： 5： 是映射到内存的大小(不是索引位置)， 即将 1.txt 的多少个字节映射到内存，
                      可以直接修改的范围是  0->5

           实际类型是 DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');

        rw.close();
        System.out.println("修改成功");
    }
}
