package com.example.spring_boot_mode.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebSocket 消息分发器
 * 负责消息的序列化和发送
 */
@Slf4j
@Component
public class MessageDispatcher {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 向指定用户发送消息
     */
    public void sendToUser(String userId, String type, Object data) {
        WebSocketSession session = SessionManager.getSession(userId);
        if (session != null && session.isOpen()) {
            sendMessage(session, type, data);
        }
    }

    /**
     * 向所有在线用户广播消息
     */
    public void broadcast(String type, Object data) {
        for (String userId : SessionManager.getOnlineUserIds()) {
            sendToUser(userId, type, data);
        }
    }

    /**
     * 向多个用户发送消息
     */
    public void sendToUsers(List<String> userIds, String type, Object data) {
        for (String userId : userIds) {
            sendToUser(userId, type, data);
        }
    }

    /**
     * 发送消息到指定会话
     * <p>
     * 失败时只记录日志、不主动关闭会话：
     * - 临时网络抖动（IO 异常）会随心跳失败 / 下一次发送自动恢复；
     * - 若仍持续异常，由容器超时 / 客户端重连机制回收资源；
     * - 避免一处广播失败导致整个链路断开（雪崩）。
     */
    private void sendMessage(WebSocketSession session, String type, Object data) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", type);
            message.put("data", data);
            message.put("timestamp", System.currentTimeMillis());

            String json = objectMapper.writeValueAsString(message);
            synchronized (session) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (IOException e) {
            // 临时 IO 异常仅记日志，不立即关闭
            log.warn("WebSocket 发送失败(type={}): {}", type, e.getMessage());
        } catch (IllegalStateException e) {
            // 并发关闭场景：另一个线程已经把会话关了
            log.warn("WebSocket 发送失败(并发关闭, type={}): {}", type, e.getMessage());
        } catch (Exception e) {
            log.error("WebSocket 发送异常(type={})", type, e);
        }
    }

    /**
     * 发送错误消息到指定会话
     */
    public void sendError(WebSocketSession session, String errorMsg) {
        try {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", errorMsg);
            errorData.put("code", 500);

            Map<String, Object> message = new HashMap<>();
            message.put("type", "error");
            message.put("data", errorData);
            message.put("timestamp", System.currentTimeMillis());

            String json = objectMapper.writeValueAsString(message);
            synchronized (session) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (Exception e) {
            // 错误消息发送失败也只记日志
            log.warn("WebSocket 错误消息发送失败: {}", e.getMessage());
        }
    }
}
