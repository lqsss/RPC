package com.rpc.netty.handle.param;

/**
 * Created by liqiushi on 2017/12/26.
 */
public class ServerRequest {
    private Long id;
    private Object content;
    private String command;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
