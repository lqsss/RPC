package com.client.annotation;

import com.remote.UserRemote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liqiushi on 2018/1/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokeTest.class)
@ComponentScan("com")
public class RemoteInvokeTest {

    @RemoteInvoke
    private UserRemote userRemote;

    @Test
    public void testGetResponse() {
        System.out.println("test");
    }

    /*    @Test
        public void testSaveUser() {
            User user =new User();
            user.setId(1);
            user.setName("zhangsan");
            Response resp = userRemote.saveUser(user);
        }*/

} 
class NULL {

    public static void print(){
        System.out.println("123");
    }
    public static void main(String[] args) {
        try{
            ((NULL)null).print();
        }catch(NullPointerException e){
            System.out.println("NullPointerException");
        }
    }
}