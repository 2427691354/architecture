package com.lixing.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.text.ParseException;

public class UDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    public UDPServerHandler() {
        super();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws ParseException, InterruptedException {

        // 读取收到的数据
        ByteBuf byteBuf=packet.content();
        //解析客户端数据
        byte[] receive=new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(receive);
//        DataParsingUtil.parse(receive,producer);


        String body = new String(receive, CharsetUtil.UTF_8);
        System.out.println("【NOTE】>>>>>> 收到客户端的数据："+body);

        // 回复一条信息给客户端
        ctx.writeAndFlush(
                new DatagramPacket(
                        Unpooled.copiedBuffer(
                                "Hello，我是Server，我的时间戳是"+System.currentTimeMillis()
                                , CharsetUtil.UTF_8), packet.sender()
                )
        ).sync();
    }

}
