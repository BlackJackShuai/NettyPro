package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 1. SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的子类
 * 2. HttpObject 客户端和服务器 端互相通讯的数据被封装成 HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //channelRead0 读取客户端的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断 msg 是不是 httpRequest 请求
        if (msg instanceof HttpRequest) {

            /**
             * 两个浏览器请求，可以发现打印出来的 hashcode 是不相同的
             * 这是因为一个 浏览器就对应一个相应的 pipeline 和 handler
             */
            System.out.println("pipeline hashcode" + ctx.pipeline().hashCode()
                    + "TestHttpServerHandler" + this.hashCode());

            System.out.println(" msg 的类型=" + msg.getClass());
            System.out.println("客户端的地址=" + ctx.channel().remoteAddress());


            //因为 http 发了两次请求，为了过滤掉 /favicon.ico
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico 资源,不做相应");
                return;
            }

            //回复信息给浏览器[满足 http 协议]
            ByteBuf content = Unpooled.copiedBuffer("Hello，我是服务器", CharsetUtil.UTF_8);

            //构造一个 http 的相应，即 httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");

            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的 response 返回
            ctx.writeAndFlush(response);

        }
    }
}
