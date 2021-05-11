package com.atguigu.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author jarvis
 * @date 2021/5/9 0009 17:05
 */
public class GroupChatServer {

    //监听端口
    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }


    //编写 run 方法，处理客户端的请求
    public void run() throws InterruptedException {

        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();//默认8个NioEventLoop
        try {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到 pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向 pipeline 加入解码器
                            pipeline.addLast("decoder", new StringDecoder());

                            pipeline.addLast("encoder", new StringDecoder());
                            //加入自己的业务处理 handler
                            pipeline.addLast(new GoupChantServerHandler());

                        }
                    });

            System.out.println("netty 服务器启动");
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            //监听关闭事件
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(7000).run();
    }
}
