package com.example.spring_boot_mode.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint(value = "/api/pushMessageMulti/{roomId}/{userId}")
public class WebSocketServerMulti {
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。*/
    private static Map<String, WebSocketServerMulti> userMap = new ConcurrentHashMap<>();
    //存放房间对象
    private static Map<String, Set<WebSocketServerMulti>> roomMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId = "";
    //存出当前群聊在线的人数（使用原因：roomMap中无法获取到当前用户id）
    private static Map<String, List<String>> multiUser = new ConcurrentHashMap<>();
    /**
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {

    }
    /**
     * 连接关闭@PathParam("userId") String userId
     * 调用的方法
     */
    @OnClose
    public void onClose( ) {

    }

    /**
     * 收到客户端消
     * 息后调用的方法
     * @param message
     * 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message) {

    }
    /**
     * 群聊
\
     */
    public void sendMessageTo()  {

    }
    /**
     * @param error
     */
    @OnError
    public void onError(Throwable error) {


    }
}