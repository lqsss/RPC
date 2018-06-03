package com.rpc.mediator;

import com.alibaba.fastjson.JSONObject;
import com.rpc.netty.handle.param.ServerRequest;
import com.rpc.netty.client.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liqiushi on 2017/12/28.
 */
public class Media {
    public static Map<String, BeanMethod> beanMap;
    private static Media media = null;
    static {
        beanMap = new HashMap<String, BeanMethod>();
    }
    
    private Media(){
    }
    
    public static Media newInstance(){
        if(media == null){
            media = new Media();
        }
        return media;
    }

    //反射处理业务
    public Response process(ServerRequest request) {
        String command = request.getCommand();
        BeanMethod beanMethod = beanMap.get(command);//接口
        Response result = null;
        if(beanMethod == null){
            return null;
        }
        Object bean = beanMethod.getBean();//接口实现类
        Method m = beanMethod.getM(); //接口里的方法实例
        Class paramType = m.getParameterTypes()[0];//参数类型
        Object content = request.getContent();//参数名

        Object args = JSONObject.parseObject(JSONObject.toJSONString(content),paramType);

        try {
            //bean 是具体实现类
            result = (Response) m.invoke(bean,args);
            result.setId(request.getId());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
