package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author jarvis
 * @date 2021/4/26 0026 22:30
 */
public class NettyByteBuf01 {

    public static void main(String[] args) {

        //创建一个ByteBuf
        /**
         * 说明：
         * 1. 创建对象，该对象包含一个数组 arr，是一个 byte[10]
         * 2. 在 netty 的 buffer 中，不需要 flip 进行反转，因为底层维护了 readerIndex 和 writerIndex
         *
         * 3.通过 readerIndex 和 writerIndex 和 capacity 讲 buffer 分成3个区域
         *
         * 0--readerIndex 已经读取的区域
         * readerIndex--writerIndex :可读的区域
         * writerIndex--capacity: 可写的区域
         */
        ByteBuf byteBuf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        //输出
//        for (int i = 0; i < byteBuf.capacity(); i++) {
//            byteBuf.getByte(i);
//        }

        for (int i = 0; i < byteBuf.capacity(); i++) {
            byteBuf.readByte();
        }
    }
}
