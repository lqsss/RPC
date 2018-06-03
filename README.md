# RPC

## 主要模块
- consumer
- rpc-user-api
- rpc-user-core
- rpc-example

## 技术栈
- Spring
- curator
- Netty
- cglib

## 服务启动
```java
@Configuration
@ComponentScan("com")
public class SpringServer {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(SpringServer.class);
    }
}
```
## example
```java
@Configuration
@ComponentScan("com")
public class BasicController {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicController.class);
        BasicService basicService = applicationContext.getBean(BasicService.class);
        basicService.testSaveUser();
    }
}
```
