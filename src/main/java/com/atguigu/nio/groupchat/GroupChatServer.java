package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 用Nio 实现一个群聊系统
 * 这是服务端
 */
public class GroupChatServer {

    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;


    //构造器
    //初始化工作
    public GroupChatServer() {
        try {

            //得到选择器
            selector = Selector.open();

            listenChannel = ServerSocketChannel.open();

            //绑定端口
            listenChannel.bind(new InetSocketAddress(PORT));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //将 listenChannel 注册到 selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {

            int count = selector.select();
            if (count > 0) {//有事件处理
                //遍历得到 selectionKey 集合
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    //取出 selectionKey
                    SelectionKey key = keyIterator.next();

                    //监听事件
                    if (key.isAcceptable()) {
                        SocketChannel sc = listenChannel.accept();
                        //设置非阻塞
                        sc.configureBlocking(false);
                        //将 sc 注册 到 selector
                        sc.register(selector, SelectionKey.OP_READ);
                        //提示
                        System.out.println(sc.getRemoteAddress() + "上线...");

                    }

                    if (key.isReadable()) { //通道发生 read 事件，即通道是可读状态
                        //处理读 （专门写方法）
                        readData(key);

                    }
                    //当前的 key 删除，防止重复处理
                    keyIterator.remove();

                }
            }


        } catch (Exception e) {

        }
    }


    //读取客户端消息
    private void readData(SelectionKey key) {
        //定义一个 SocketChannel

        SocketChannel channel = null;

        try {

            channel = (SocketChannel) key.channel();
            //创建 Buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            //将 channel 的数据读到 Buffer
            int count = channel.read(byteBuffer);

            if (count > 0) {
                //把缓存区的数据转化成字符串
                String str = new String(byteBuffer.array());
                //输出该消息
                System.out.println("from 客户端" + str);

                //向其它的客户端转发消息(去掉自己)，专门写一个方法处理
                sendInfoToOtherClients(str, channel);
            }

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


    //转发消息可其他客户端（通道）
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        //服务器转发消息
        //遍历 所有注册到 selector 上的SocketChannel ,并排除自己
        for (SelectionKey key : selector.keys()) {
            //通过 key 取出对应的 SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {

                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将 msg 存储到 buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将 buffer的数据写入到 通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }

}
