package com.user.remote;

import com.model.User;
import com.remote.UserRemote;
import com.rpc.annotation.Remote;
import com.user.service.UserService;
import com.util.ResponseUtil;

import javax.annotation.Resource;

/**
 * Created by liqiushi on 2018/1/2.
 */
@Remote
public class UserRemoteImpl implements UserRemote {
    @Resource
    private UserService userService;
//test
    public Object saveUser(User user){
        userService.save(user);
        return ResponseUtil.createSuccessResult(user);
    }
}
