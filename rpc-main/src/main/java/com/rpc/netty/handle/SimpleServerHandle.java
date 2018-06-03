package com.rpc.netty.handle;

import com.alibaba.fastjson.JSONObject;
import com.rpc.netty.handle.param.ServerRequest;
import com.rpc.netty.client.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by liqiushi on 2017/12/20.
 */
public class SimpleServerHandle extends ChannelInboundHandlerAdapter {
    private int loss_connect_time = 0;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("connect!");
        //ctx.channel().writeAndFlush("hello\r\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("disconnected!");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("test");
        ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);
        Response resp = new Response();
        resp.setId(request.getId());
        resp.setResult("is ok");
        ctx.channel().writeAndFlush(JSONObject.toJSONString(resp));

        if (msg.equals("Heartbeat")) {
            System.out.println("recv Heartbeat!");
            ctx.write("has read message from server");
            ctx.flush();
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                System.out.println(loss_connect_time);
                if (loss_connect_time > 2) {
                    System.out.println("close");
                    //ctx.close();
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
