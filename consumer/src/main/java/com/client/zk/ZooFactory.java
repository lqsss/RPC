package com.client.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by liqiushi on 2017/12/20.
 */
public class ZooFactory {
    public static CuratorFramework client;
    
        public static CuratorFramework create(){
       if(client == null){
           RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
           client = CuratorFrameworkFactory.newClient("localhost:2181",retryPolicy);
           client.start();
       }
        return client;
    }

    public static void main(String[] args) {
        CuratorFramework client = create();
        try {
            client.create().forPath("/netty/lqs_pc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
