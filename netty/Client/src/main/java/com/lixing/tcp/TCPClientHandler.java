package com.lixing.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.DatagramPacket;

/**
 * @author cc
 * @date 2020/06/12
 **/
public class TCPClientHandler extends SimpleChannelInboundHandler {


    /*
     * ChannelInboundHandlerAdapter：ChannelInboundHandlerAdapter是ChannelInboundHandler的一个简单实现，默认情况下不会做任何处理，
     *   只是简单的将操作通过fire*方法传递到ChannelPipeline中的下一个ChannelHandler中让链中的下一个ChannelHandler去处理。
     *
     * SimpleChannelInboundHandler：SimpleChannelInboundHandler支持泛型的消息处理，默认情况下消息处理完将会被自动释放，无法提供
     *   fire*方法传递给ChannelPipeline中的下一个ChannelHandler,如果想要传递给下一个ChannelHandler需要调用ReferenceCountUtil#retain方法。
     * */

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ServerHandler receive msg:"+msg.toString());
        //写消息：先得到channel，在写如通道然后flush刷新通道把消息发出去。
        // ctx.channel().writeAndFlush("this is ServerHandler reply msg happend at !"+System.currentTimeMillis());

    }
}
