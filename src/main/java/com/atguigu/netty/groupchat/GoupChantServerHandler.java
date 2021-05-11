package com.atguigu.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jarvis
 * @date 2021/5/9 0009 17:18
 */
public class GoupChantServerHandler extends SimpleChannelInboundHandler<String> {


    //定义一个 channel 组，管理着所有的 channel
    //GlobalEventExecutor.INSTANCE 是全局的执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //handlerAdded 标识连接一旦成功，这个方法第一个被执行
    //将当前 channel 加入到 channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户端
        /**
         * writeAndFlush 方法会将 channelGroup 中所有的 channel 遍历，发送消息
         * 我们自己就不需要遍历了
         */
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天\n" + sdf.format(new Date()));
        channelGroup.add(channel);
    }


    //断开连接，将**客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");
        System.out.println("channelGroup size" + channelGroup.size());
    }

    //表示 channel 处于一个活跃的状态，提示 ** 上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了~");
    }

    //标识 channel 处于一个非活跃的状态，提示 ** 离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了~");
    }

    //读取数据，把读取的数据转发给其它客户端
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        Channel channel = ctx.channel();
        //这里我们遍历 channelGroup ,根据不同的情况，回送不同的消息
        channelGroup.forEach(ch -> {
            if (ch != channel) {  //不是当前 channel,就转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送了消息" + msg + "\n");
            } else {//回显自己发送的消息给自己
                ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });
    }


    //发送了异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
