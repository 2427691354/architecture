package com.lixing.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author cc
 * @date 2020/06/12
 **/
public class TCPHandler2 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ServerHandler2 receive msg:"+msg.toString()+"xxxxx");
        ctx.channel().writeAndFlush("this is ServerHandler2 reply msg happend at !"+System.currentTimeMillis());
    }
}
