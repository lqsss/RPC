package com.rpc.mediator;

import java.lang.reflect.Method;

/**
 * Created by liqiushi on 2017/12/28.
 */
public class BeanMethod {
    private Object bean;
    private Method m;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getM() {
        return m;
    }

    public void setM(Method m) {
        this.m = m;
    }
}
