package com.service;

import com.alibaba.fastjson.JSONObject;
import com.client.annotation.RemoteInvoke;
import com.model.User;
import com.remote.UserRemote;
import org.springframework.stereotype.Service;

/**
 * Created by liqiushi on 2018/1/7.
 */
@Service
public class BasicService {
    @RemoteInvoke
    private UserRemote userRemote; //注入的时候，将这个field换成enhancer.create
    public void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setName("zhangsan");
        Object resp = userRemote.saveUser(user);
        System.out.println(JSONObject.toJSONString(resp));
    }
}
