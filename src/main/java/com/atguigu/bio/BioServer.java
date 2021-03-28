package com.atguigu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jarvis
 * @date 2021/3/28 0028 10:49
 */
public class BioServer {

    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        ServerSocket socket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while (true) {
            //监听，等待客户端连接
            final Socket accept = socket.accept();
            System.out.println("连接到了一个客户端");

            //就创建一个线程，与之通讯，（单独写一个方法）
            threadPool.execute(new Runnable() {
                public void run() {
                    //可以和客户端通讯
                    handler(accept);
                }
            });
        }
    }


    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];

        try {
            System.out.println("线程信息id=" + Thread.currentThread().getId() + "名字="
                    + Thread.currentThread().getName());
            //通过socket 获取到输入流
            InputStream inputStream = socket.getInputStream();

            //循环读取客户端发送的数据
            while (true) {
                System.out.println("reading........!!!!");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //输出客户端发送的数据
                    System.out.println(new String(bytes, 0, read));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭socket
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}
