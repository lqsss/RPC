package com.util;

import com.rpc.netty.client.Response;

/**
 * Created by liqiushi on 2017/12/29.
 */
public class ResponseUtil {
    public static Response createSuccessResult() {
        return new Response();
    }

    public static Response createFailResult(String code, String msg) {
        Response result = new Response();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    
    public static Response createSuccessResult(Object content){
        Response result = new Response();
        result.setResult(content);
        return result;
    }
}
