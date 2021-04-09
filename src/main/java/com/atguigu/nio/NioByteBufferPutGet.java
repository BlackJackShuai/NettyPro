package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer 支持类型化的 put 和 get ，put 放入什么数据类型，get 就应该使用相应的数据类型取出
 * 否则就可能会 BufferUnderflowException
 */
public class NioByteBufferPutGet {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        //类型化方式放入数据
        buffer.putInt(11);
        buffer.putLong(9L);
        buffer.putChar('A');
        buffer.putShort((short) 4);

        //取出
        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());


    }
}
