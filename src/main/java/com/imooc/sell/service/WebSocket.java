package com.imooc.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;


//老师说这不是controller，但是service也不合适，就放service吧
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session; //这个相当于用来记录 每次请求来的socket

    //老师说定义这个容器 来储存session。另外老师还说为啥不用普通的 Set 呢。是有原因地
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    //这里定义事件
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】有新的连接，总数：{}", webSocketSet.size());
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        log.info("【websocket消息】连接断开，总数：{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message){
        log.info("【websocket消息】收到客户端发来的消息：{}", message);
    }

    //这个相当于 广播
    public void sendMessage(String message){
        for (WebSocket webSocket: webSocketSet){ //遍历所有 webSocket 然后发送 message
            log.info("【websocket消息】广播消息，message={}", message);
            try{
                webSocket.session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
