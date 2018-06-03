package com.client.core;

/**
 * Created by liqiushi on 2017/12/21.
 */

import com.alibaba.fastjson.JSONObject;
import com.client.constant.Constants;
import com.client.handle.ClientHandler;
import com.client.param.ClientRequest;
import com.client.param.Response;
import com.client.zk.ZooFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;

import java.util.List;

public class TcpClient {

    static final Bootstrap b = new Bootstrap();
    static EventLoopGroup workGroup = new NioEventLoopGroup();
    static ChannelFuture f = null;

    static {
        b.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //ch.pipeline().addLast("ping", new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        String host = "localhost";
        int port = -1;
        CuratorFramework client = ZooFactory.create();
        try {
            List<String> serverPaths = client.getChildren().forPath(Constants.SERVER_PATH);
            //zk监听
            CuratorWatcher watcher = new ServerWatcher();
            client.getChildren().usingWatcher(watcher).forPath(Constants.SERVER_PATH);

            for (String serverpath : serverPaths) {
                String[] strings = serverpath.split("#");
                int weight = Integer.valueOf(strings[2]);
                if (weight > 0) {
                    for (int w = 0; w < weight; w++) {
                        ChannelManager.realServerPath.add(strings[0]+"#"+strings[1]);
                        ChannelFuture channelFuture = b.connect(host, port);
                        ChannelManager.addChannel(channelFuture);
                    }
                }
            }
            if (ChannelManager.realServerPath.size() > 0) {
                String[] strings = ChannelManager.realServerPath.toArray()[0].toString().split("#");
                host = strings[0];
                port = Integer.parseInt(strings[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
/*        try {
            f = b.connect(new InetSocketAddress(host, port)).sync();
        *//*    ChannelFuture connectFuture = b.connect(new InetSocketAddress("127.0.0.1", 8001)).sync();
            connectFuture.channel().closeFuture().sync();*//*
        } catch (InterruptedException e) {
            e.printStackTrace();
    }*/
    }


    //发送数据 
    public static Response send(ClientRequest request) {
        f = ChannelManager.get(ChannelManager.pos);
        f.channel().writeAndFlush(JSONObject.toJSONString(request));
        f.channel().writeAndFlush("\r\n");
        DefaultFuture df = new DefaultFuture(request);
        return df.get(df.getTimeout());
    }
}
