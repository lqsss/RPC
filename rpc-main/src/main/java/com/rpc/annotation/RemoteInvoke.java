package com.rpc.annotation;

import java.lang.annotation.*;

/**
 * Created by liqiushi on 2018/1/2.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoteInvoke {
    
}
