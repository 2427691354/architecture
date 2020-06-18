package com.lixing.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * netty客户端
 */
public class TCPClient {
    public static void main(String[] args) {
        startClient("127.0.0.1",8888);
    }

    public static void startClient(String HOST,Integer PORT){
        //1.定义服务类
        Bootstrap clientBootstap = new Bootstrap();

        //2.定义执行线程组
        EventLoopGroup worker = new NioEventLoopGroup();

        //3.设置线程池
        clientBootstap.group(worker);

        //4.设置通道 - 代表异步的客户端 TCP Socket 连接.
        clientBootstap.channel(NioSocketChannel.class);

        //5.添加Handler
        clientBootstap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                System.out.println("client channel init!");
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("StringDecoder",new StringDecoder());
                pipeline.addLast("StringEncoder",new StringEncoder());
                pipeline.addLast("ClientHandler",new TCPClientHandler());
            }
        });

        //6.建立连接
        ChannelFuture channelFuture = clientBootstap.connect(HOST,PORT);
        try {
            //7.测试输入
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                System.out.println("请输入：");
                String msg = bufferedReader.readLine();
                channelFuture.channel().writeAndFlush(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //8.关闭连接
            worker.shutdownGracefully();
        }
    }
}
