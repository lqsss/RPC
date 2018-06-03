package com.client.proxy;

import com.client.param.Response;
import com.client.annotation.RemoteInvoke;
import com.client.core.TcpClient;
import com.client.param.ClientRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liqiushi on 2018/1/2.
 */
@Component
public class InvokeProxy implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName);
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            /**
             *isAnnotationPresent
             *如果此元素上存在指定类型的注释，则返回true，否则返回false。
             *此方法主要是为了方便访问标记注释而设计的。
             */
            if (field.isAnnotationPresent(RemoteInvoke.class)) {
                final Map<Method, Class> methodClassMap = new HashMap<Method, Class>();
                putMethodClass(methodClassMap, field);
                field.setAccessible(true);
                //动态代理
                Enhancer enhancer = new Enhancer();
                enhancer.setInterfaces(new Class[]{field.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        ClientRequest request = new ClientRequest();
                        request.setCommand(methodClassMap.get(method).getName() + "." + method.getName());
                        request.setContent(objects[0]);
                        Response resp = TcpClient.send(request);
                        return resp;
                    }
                });
                try {
                    field.set(bean, enhancer.create());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
        Method[] methods = field.getType().getDeclaredMethods();
        for (Method m : methods) {
            methodClassMap.put(m, field.getType());
        }
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
