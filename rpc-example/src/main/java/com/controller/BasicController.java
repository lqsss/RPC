package com.controller;

import com.service.BasicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liqiushi on 2018/1/7.
 */
@Configuration
@ComponentScan("com")
public class BasicController {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicController.class);
        BasicService basicService = applicationContext.getBean(BasicService.class);
        basicService.testSaveUser();
    }
}
