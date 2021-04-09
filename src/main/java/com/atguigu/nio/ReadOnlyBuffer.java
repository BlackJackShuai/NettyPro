package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * @author jarvis
 * @date 2021/4/4 0004 16:46
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) {
        //c创建一个 buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 6; i++) {
            buffer.put((byte) i);
        }

        //读取
        buffer.flip();

        //得到一个只读的 buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        //读取
        while (readOnlyBuffer.hasArray()) {
            System.out.println(readOnlyBuffer.get());
        }


        readOnlyBuffer.put((byte) 100);   //ReadOnlyBufferException
    }
}
