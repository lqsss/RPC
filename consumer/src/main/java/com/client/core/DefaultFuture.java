package com.client.core;

import com.client.param.Response;
import com.client.param.ClientRequest;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
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
    private long timeout = 2 * 60 * 1000l;
    private long startTime = System.currentTimeMillis();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getStartTime() {
        return startTime;
    }

    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(), this);
    }


    public Response get(long time) {
        lock.lock();
        try {
            while (!done()) {
                //TODO
                condition.await(time, TimeUnit.SECONDS);
                if (System.currentTimeMillis() - startTime > time) {
                    System.out.println("request timeout!");
                    break;
                }
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

    static class FutureThread extends Thread {
        
        @Override
        public void run() {
            Set<Long> ids = allDefaultFuture.keySet();
            for (Long id :ids) {
                DefaultFuture df = allDefaultFuture.get(id);
                if(df == null){
                    allDefaultFuture.remove(id);
                }else{
                    if(df.getTimeout() <(System.currentTimeMillis() - df.getStartTime())){
                        Response resp =  new Response();
                        resp.setId(id);
                        resp.setCode("33333");
                        resp.setMsg("请求超时");
                        recive(resp);
                    }
                }
            }
        }
    }
    
    static{
        FutureThread futureThread = new FutureThread();
        futureThread.setDaemon(true);
        futureThread.start();
    }
}


