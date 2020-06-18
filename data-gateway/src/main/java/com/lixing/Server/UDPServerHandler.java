package com.lixing.Server;

import com.lixing.util.DataParsingUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.kafka.clients.producer.Producer;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Hashtable;

public class UDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private Producer<String, String> producer;

    public UDPServerHandler(Producer<String, String> producer) {
        super();
        this.producer = producer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws  ParseException {

        //datagramPacket 为接收到的客户端传递的数据
        ByteBuf byteBuf=datagramPacket.content();
        //解析客户端数据
        byte[] receive=new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(receive);
        DataParsingUtil.parse(receive,producer);

    }

}
