package com.lixing.Server;

import com.lixing.util.DataParsingUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author cc
 * @date 2020/06/10
 **/
public class HeartBeatHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,  String s) throws Exception {
        System.err.println(" ====== > [server] message received : " + s);
    }



}
