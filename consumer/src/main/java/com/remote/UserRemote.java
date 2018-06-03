package com.remote;


import com.bean.User;
import com.client.param.Response;

/**
 * Created by liqiushi on 2018/1/2.
 */
public interface UserRemote {
    //test
     Response saveUser(User user);
}
