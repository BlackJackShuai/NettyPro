package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 1.我们自定义一个 handler 需要继承 netty 规定好的某个 HandlerAdapter
 * 2.这时我们自定义一个 Handler，才能称为称为一个 handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    //读取数据实际（这里我们可以读取客户端发送的数据）

    /**
     * 1.ChannelHandlerContext ctx:上下文对象，含有 管道 pipeline，通道 channel ，地址
     * 2.Object msg:就是客户端发送的数据 默认为 Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //比如我们这里有一个非常耗时长的业务 -> 异步执行 -> 提交该 channel 对应的
        //NIOEventLoop 的 taskQueue 中

        //解决方案1： 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello 客户端 喵2", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }

        });


        //2.用户自定义定时任务 -> 该任务提交到 scheduleTaskQueue 中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello 客户端 喵4", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }

        }, 5, TimeUnit.SECONDS);

//        System.out.println("server ctx" + ctx);
//        //讲 msg 转成一个 ByteBuf
//        //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端的地址是：" + ctx.channel().remoteAddress());
    }


    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是 write + flush
        //将数据写到缓存，并刷新
        //一般来讲，我们会对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端  ", CharsetUtil.UTF_8));
    }


    //处理异常，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
