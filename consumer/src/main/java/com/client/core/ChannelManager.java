package com.client.core;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liqiushi on 2018/1/8.
 */
public class ChannelManager {
    static CopyOnWriteArrayList<String> realServerPath = new CopyOnWriteArrayList<String>();
    
    static AtomicInteger pos = new AtomicInteger(0);
    
    public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<ChannelFuture>();

    public static void removeChannel(ChannelFuture channel) {
        channelFutures.remove(channel);
    }

    public static void addChannel(ChannelFuture channel) {
        channelFutures.add(channel);
    }

    public static void clearChannel() {
        channelFutures.clear();
    }

    public static ChannelFuture get(AtomicInteger i) {
        ChannelFuture f = null;
        int size = channelFutures.size();
        if (i.get() > size) {
            f = channelFutures.get(0);
            pos = new AtomicInteger(1);
        } else {
            f = channelFutures.get(i.getAndIncrement());
        }
        return f;
    }
}
 