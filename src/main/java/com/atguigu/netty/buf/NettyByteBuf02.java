package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author jarvis
 * @date 2021/4/26 0026 22:30
 */
public class NettyByteBuf02 {

    public static void main(String[] args) {

        //创建一个ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello,Hello", Charset.forName("utf-8"));

        //使用相关的方法
        if (byteBuf.hasArray()) {  //true
            byte[] content = byteBuf.array();

            //将 content 转回去
            System.out.println(new String(content, Charset.forName("utf-8")));

            System.out.println("byteBuf=" + byteBuf);

            System.out.println(byteBuf.arrayOffset()); //0
            System.out.println(byteBuf.readerIndex()); //0
            System.out.println(byteBuf.writerIndex()); //11
            System.out.println(byteBuf.capacity());//36

            int len = byteBuf.readableBytes();  //可读的字节数
            System.out.println("len=" + len);


        }

    }
}
