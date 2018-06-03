package com.rpc.netty.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liqiushi on 2017/12/24.
 */
public class DefaultFuture {
    public static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture = new ConcurrentHashMap<Long, DefaultFuture>();
    private Response response;
    final Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(), this);
    }


    public Response get() {
        lock.lock();
        try {
            while (!done()) {
                condition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return response;
    }

    public static void recive(Response response) {
        DefaultFuture df = allDefaultFuture.get(response.getId());
        if (df != null) {
            Lock lock = df.lock;
            lock.lock();
            df.setResponse(response);
            df.condition.signal();
            lock.unlock();
        }
    }

    private boolean done() {
        if (this.response != null) {
            return true;
        }
        return false;
    }
}
