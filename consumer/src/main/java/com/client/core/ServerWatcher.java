package com.client.core;

import com.client.zk.ZooFactory;
import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.util.List;

/**
 * Created by liqiushi on 2018/1/8.
 */
public class ServerWatcher implements CuratorWatcher {
    public void process(WatchedEvent watchedEvent) throws Exception {
        CuratorFramework client = ZooFactory.create();
        String path = watchedEvent.getPath();
        client.getChildren().usingWatcher(this).forPath(path);
        List<String> serverPaths = client.getChildren().forPath(path);

        for (String serverpath : serverPaths) {
            String host = null;
            int port = -1;
            String[] strings = serverpath.split("#");
            host = strings[0];
            port = Integer.valueOf(strings[1]);
            int weight = Integer.valueOf(strings[2]);
            if (weight > 0) {
                for (int w = 0; w < weight; w++) {
                    ChannelManager.realServerPath.add(host + "#" + port);
                }
            }
            ChannelManager.realServerPath.add(host + "#" + port);
        }
        ChannelManager.clearChannel();
        for (String realServer : ChannelManager.realServerPath) {
            String host = null;
            int port = -1;
            String[] strings = realServer.split("#");
            host = strings[0];
            port = Integer.valueOf(strings[1]);
            int weight = Integer.valueOf(strings[2]);
            if (weight > 0) {
                for (int w = 0; w < weight; w++) {
                    ChannelManager.realServerPath.add(strings[0] + "#" + strings[1]);
                    ChannelFuture channelFuture = TcpClient.b.connect(host, port);
                    ChannelManager.addChannel(channelFuture);
                }
            }
        }
    }
}
