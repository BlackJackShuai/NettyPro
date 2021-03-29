package com.atguigu.nio;

import java.nio.IntBuffer;

/**
 * @author jarvis
 * @date 2021/3/28 0028 17:21
 */
public class BasicBuffer {

    public static void main(String[] args) {

        //创建一个 buffer,大小为5，即可以存放 5个 int
        IntBuffer buffer = IntBuffer.allocate(5);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put(i * 2);
        }

        //如何从buffer 中读取数据
        //讲 buffer 转换，读写切换
        buffer.flip();

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
