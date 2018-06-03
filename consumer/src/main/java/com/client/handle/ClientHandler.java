package com.client.handle;

/**
 * Created by liqiushi on 2017/12/21.
 */
import com.client.core.DefaultFuture;
import com.client.param.Response;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.util.Date;

/**
 * Created by liqiushi on 2017/12/14.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf firstSendMsg;

    private int currentTime = 0;
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
            CharsetUtil.UTF_8));
    public ClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstSendMsg = Unpooled.buffer(req.length);
        firstSendMsg.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Send firstSendMsg!");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().attr(AttributeKey.valueOf("attrTest")).set(msg);
        System.out.println("Client read!");
        String str = (String) msg;
        System.out.println("Now is :" + str);
        Response response = JSONObject.parseObject(msg.toString(),Response.class);
        DefaultFuture.recive(response);
        //ctx.close();
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("EventTriggered TIME : "+ new Date(System.currentTimeMillis()));
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state() == IdleState.WRITER_IDLE){
                if(currentTime < 3){
                    System.out.println("currentTime : "+ currentTime);
                    currentTime++;
                    ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}