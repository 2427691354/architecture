package com.lixing.Server;

import com.lixing.util.DataParsingUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.kafka.clients.producer.Producer;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class UDPServer {
    /**
     * 启动netty服务
     * @throws InterruptedException
     */

    public static void run(Integer PORT,Producer<String, String> producer) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        //NioSocketChannel, 代表异步的客户端 TCP Socket 连接.
        //NioServerSocketChannel, 异步的服务器端 TCP Socket 连接.
        //NioDatagramChannel, 异步的 UDP 连接
        //NioSctpChannel, 异步的客户端 Sctp 连接.
        //NioSctpServerChannel, 异步的 Sctp 服务器端连接.
        //OioSocketChannel, 同步的客户端 TCP Socket 连接.
        //OioServerSocketChannel, 同步的服务器端 TCP Socket 连接.
        //OioDatagramChannel, 同步的 UDP 连接
        //OioSctpChannel, 同步的 Sctp 服务器端连接.
        //OioSctpServerChannel, 同步的客户端 TCP Socket 连接.

        b.group(group).channel(NioDatagramChannel.class).handler(new UDPServerHandler(producer));


        // 服务端监听在PORT端口
        b.bind(PORT).sync().channel().closeFuture().await();


    }


}
