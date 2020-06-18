package com.lixing.Server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.DatagramPacket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * @author cc
 * @date 2020/06/10
 **/
class MyHeartbeatChannelHandler extends
        SimpleChannelInboundHandler<DatagramPacket> implements Runnable {
    private Hashtable<String, Long> observe = new Hashtable<String, Long>();

    public MyHeartbeatChannelHandler() {
        new Thread(this).start();
    }

    @Override
    protected void channelRead0(
            ChannelHandlerContext paramChannelHandlerContext,
            DatagramPacket paramI) throws Exception {
        System.err.println(paramI.toString());

    }

    private int flag = 0;

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg)
//            throws Exception {
//        synchronized (Object.class) {
//            if (msg instanceof DatagramPacket) {
//                System.err.println(msg.toString());
//                observe.put(((DatagramPacket) msg).toString(),
//                        System.currentTimeMillis());
//            }
//            System.out.println(msg instanceof DatagramPacket);
//        }
//    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (Object.class) {
                Set<String> keys = observe.keySet();
                Iterator<String> mIterator = keys.iterator();
                while (mIterator.hasNext()) {
                    String k = mIterator.next();
                    long l = observe.get(k);
                    if (l > 0 && System.currentTimeMillis() - l > 3 * 1000) {
                        mIterator.remove();
                        System.out.println(k + "掉线");
                    }
                }
            }
        }
    }
}

