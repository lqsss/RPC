/*
package rpc.annotation;

import RemoteInvoke;
import com.com.user.bean.User;
import com.com.user.remote.UserRemote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

*/
/**
 * Created by liqiushi on 2018/1/2.
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokeTest.class)
@ComponentScan("com.rpc")
public class RemoteInvokeTest {
    
    @RemoteInvoke
    private UserRemote userRemote;

    @Test
    public void testGetResponse() {

    }

    @Test
    public void testSaveUser() {
        User com.user =new User();
        com.user.setId(1);
        com.user.setName("zhangsan");
        userRemote.saveUser(com.user);
    }
}
*/
