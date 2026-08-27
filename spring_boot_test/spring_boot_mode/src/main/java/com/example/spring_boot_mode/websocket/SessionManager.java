package com.example.spring_boot_mode.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 会话管理器
 * 管理所有在线用户的 WebSocket 连接
 * 使用 ConcurrentHashMap 保证线程安全
 */
public class SessionManager {

    // 用户ID → 会话映射
    private static final Map<String, WebSocketSession> USER_SESSIONS = new ConcurrentHashMap<>();

    // 会话ID → 用户ID 映射 (用于反查)
    private static final Map<String, String> SESSION_USERS = new ConcurrentHashMap<>();

    /**
     * 注册用户会话
     * 如果用户已在线，先关闭旧连接再注册新连接
     *
     * @param userId  用户ID
     * @param session WebSocket会话
     */
    public static void register(String userId, WebSocketSession session) {
        // 如果用户已在线，先关闭旧连接
        WebSocketSession oldSession = USER_SESSIONS.get(userId);
        if (oldSession != null && oldSession.isOpen()) {
            try {
                // 先清理旧sessionId映射，避免残留导致后续误删新连接
                SESSION_USERS.remove(oldSession.getId());
                oldSession.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
        USER_SESSIONS.put(userId, session);
        SESSION_USERS.put(session.getId(), userId);
    }

    /**
     * 注销用户会话
     *
     * @param sessionId 会话ID
     */
    public static void unregister(String sessionId) {
        String userId = SESSION_USERS.remove(sessionId);
        if (userId != null) {
            // 仅当当前 userId → session 仍然指向该 sessionId 时才移除
            // 防止“旧连接关闭事件”把新连接误踢下线（典型竞态）
            WebSocketSession current = USER_SESSIONS.get(userId);
            if (current != null && sessionId.equals(current.getId())) {
                USER_SESSIONS.remove(userId);
            }
        }
    }

    /**
     * 根据用户ID获取会话
     *
     * @param userId 用户ID
     * @return WebSocket会话，如果不存在返回null
     */
    public static WebSocketSession getSession(String userId) {
        return USER_SESSIONS.get(userId);
    }

    /**
     * 获取所有在线用户ID集合
     *
     * @return 在线用户ID集合
     */
    public static Set<String> getOnlineUserIds() {
        return USER_SESSIONS.keySet();
    }

    /**
     * 获取在线用户数量
     *
     * @return 在线用户数量
     */
    public static int getOnlineCount() {
        return USER_SESSIONS.size();
    }

    /**
     * 检查用户是否在线
     *
     * @param userId 用户ID
     * @return true-在线，false-离线
     */
    public static boolean isOnline(String userId) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        return session != null && session.isOpen();
    }
    
    /**
     * 检查用户是否在线（别名方法，保持兼容性）
     *
     * @param userId 用户ID
     * @return true-在线，false-离线
     */
    public static boolean isUserOnline(String userId) {
        return isOnline(userId);
    }

    /**
     * 根据会话ID获取用户ID
     *
     * @param sessionId 会话ID
     * @return 用户ID
     */
    public static String getUserIdBySessionId(String sessionId) {
        return SESSION_USERS.get(sessionId);
    }

    /**
     * 获取用户ID与会话的映射（只读副本）
     *
     * @return 用户会话映射
     */
    public static Map<String, WebSocketSession> getUserSessions() {
        return new ConcurrentHashMap<>(USER_SESSIONS);
    }
}
