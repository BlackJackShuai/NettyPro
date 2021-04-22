package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author jarvis
 * @date 2021/4/18 0018 22:53
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //创建 BossGroup 和 WorkGroup
        /**
         * 说明：
         * 1.创建两个线程组 bossGroup 和 workGroup
         * 2.bossGroup 只是处理连接请求，真正和客户端业务处理，会交给 workGroup 完成
         * 3.两个都是无限循环
         * 4. bossGroup 和 workGroup 含有子线程（NioEventLoop）的个数
         * 默认实际 cpu 核数 * 2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {

            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            bootstrap.group(bossGroup, workGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象（匿名对象）
                        //给 pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) {

                            //可以使用一个集合管理 SocketChannel，再推送消息，可以将业务加入到各个 channel
                            //对应的 NioEvenLoop 的 taskQueue 或者 scheduleTaskQueue
                            System.out.println("客户端 socketChannel hashcode=" + ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });//给我们的 workGroup 的 EventLoop 对应的管道设置处理器


            System.out.println("Server is reading....");

            //绑定一个端口并且同步，生成一个 ChannelFuture 对象
            //启动服务器（并绑定端口）
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给 cf 注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();

        } finally {
            //优雅地关闭
            bossGroup.shutdownGracefully();
        }
    }
}
