package com.rpc.mediator;

import com.rpc.annotation.Remote;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by liqiushi on 2017/12/27.
 */

//bean初始化后执行的方法
@Component
public class InitialMedium implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName);
        if (bean.getClass().isAnnotationPresent(Remote.class)) {//类
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method m : methods) {
                //UserRemote.saveUser()
                String key = bean.getClass().getInterfaces()[0].getName() + "." + m.getName();
                Map<String,BeanMethod> beanMap = Media.beanMap;
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setM(m);
                beanMap.put(key,beanMethod);
            }
            System.out.println(bean.getClass().getName());
        }
        return bean;
    }
}
