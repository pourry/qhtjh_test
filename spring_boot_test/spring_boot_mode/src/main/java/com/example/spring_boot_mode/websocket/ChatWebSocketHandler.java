package com.example.spring_boot_mode.websocket;

import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.model.dao.messageCenter.ChatMessageDao;
import com.example.spring_boot_mode.model.dao.messageCenter.OnlineUserDao;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.model.entity.messageCenter.ChatMessage;
import com.example.spring_boot_mode.model.service.messageCenter.ChatRateLimiter;
import com.example.spring_boot_mode.utils.ChatContentSanitizer;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

/**
 * 聊天室 WebSocket 处理器
 * 处理所有 WebSocket 连接和消息
 * 支持：聊天消息、心跳检测、输入状态、在线/离线通知
 */
@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private ChatMessageDao chatMessageDao;

    @Autowired
    private OnlineUserDao onlineUserDao;

    @Autowired
    private MessageDispatcher dispatcher;

    @Autowired
    private ChatRateLimiter chatRateLimiter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立时触发
     * 验证Token，注册会话，通知其他用户
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = validateToken(session);
        if (userId == null) {
            log.warn("WebSocket连接失败：Token无效");
            try {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
            } catch (Exception e) {
                // 忽略
            }
            return;
        }

        // 注册会话
        SessionManager.register(userId, session);
        session.getAttributes().put("userId", userId);

        // 更新用户在线状态
        updateOnlineStatus(userId, true);

        // 获取用户信息
        SysUser user = loginDao.selectById(userId);
        Map<String, Object> userInfo = getUserInfo(user);

        // 获取在线用户列表
        List<Map<String, Object>> onlineUsers = getOnlineUserList();

        // 发送连接成功确认给当前用户
        Map<String, Object> connectData = new HashMap<>();
        connectData.put("userId", userId);
        connectData.put("userInfo", userInfo);
        connectData.put("onlineUsers", onlineUsers);
        dispatcher.sendToUser(userId, "connect", connectData);

        // 通知其他用户：有新用户上线
        Map<String, Object> onlineData = new HashMap<>();
        onlineData.put("user", userInfo);
        onlineData.put("onlineUsers", onlineUsers);
        // 不通知自己，避免客户端出现“自己上线了”的系统提示
        Set<String> onlineUserIds = SessionManager.getOnlineUserIds();
        for (String uid : onlineUserIds) {
            if (!uid.equals(userId)) {
                dispatcher.sendToUser(uid, "online", onlineData);
            }
        }

        log.info("用户 {} 已连接WebSocket，当前在线人数：{}", userId, SessionManager.getOnlineCount());
    }

    /**
     * 连接关闭时触发
     * 更新用户离线状态，通知其他用户
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();
        String userId = (String) session.getAttributes().get("userId");

        // 注销会话
        SessionManager.unregister(sessionId);

        // 可能存在竞态：旧连接关闭不应把新连接“误下线”
        if (userId != null && !SessionManager.isOnline(userId)) {
            // 更新用户离线状态
            updateOnlineStatus(userId, false);

            // 通知其他用户：用户下线
            List<Map<String, Object>> onlineUsers = getOnlineUserList();
            Map<String, Object> offlineData = new HashMap<>();
            offlineData.put("userId", userId);
            offlineData.put("onlineUsers", onlineUsers);
            dispatcher.broadcast("offline", offlineData);

            log.info("用户 {} 已断开WebSocket，当前在线人数：{}", userId, SessionManager.getOnlineCount());
        }
    }

    /**
     * 收到消息时触发
     * 根据消息类型分发处理
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            JsonNode root = objectMapper.readTree(message.getPayload());
            String type = root.get("type").asText();
            JsonNode data = root.get("data");
            String messageId = root.has("messageId") ? root.get("messageId").asText() : null;

            switch (type) {
                case "chat":
                    handleChatMessage(session, data, messageId);
                    break;
                case "heartbeat":
                    handleHeartbeat(session, messageId);
                    break;
                case "typing":
                    handleTypingMessage(session, data);
                    break;
                default:
                    log.warn("未知的消息类型: {}", type);
            }
        } catch (Exception e) {
            log.error("处理消息异常", e);
            dispatcher.sendError(session, "消息处理失败");
        }
    }

    /**
     * 处理聊天消息
     * 保存消息到数据库并广播给所有在线用户
     */
    private void handleChatMessage(WebSocketSession session, JsonNode data, String messageId) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId == null) return;

        if (data == null || !data.hasNonNull("content")) {
            dispatcher.sendError(session, "消息内容不能为空");
            return;
        }
        String content = data.get("content").asText();
        String messageType = data.hasNonNull("messageType") ? data.get("messageType").asText() : "text";
        // 限制消息类型，避免客户端随意注入未知类型导致渲染/存储异常
        if (!"text".equals(messageType) && !"system".equals(messageType)) {
            messageType = "text";
        }

        // 验证消息内容
        if (content == null || content.trim().isEmpty()) {
            dispatcher.sendError(session, "消息内容不能为空");
            return;
        }
        if (content.length() > 500) {
            dispatcher.sendError(session, "消息内容不能超过500字符");
            return;
        }

        // 限流：WS 路径与 HTTP 共享同一限流器（避免绕过）
        if (!chatRateLimiter.tryAcquire(userId)) {
            dispatcher.sendError(session, "发送过于频繁，请稍后再试");
            return;
        }

        // XSS/输入清洗
        String safeContent = ChatContentSanitizer.sanitize(content);
        if (safeContent.isEmpty()) {
            dispatcher.sendError(session, "消息内容不能为空");
            return;
        }
        if (safeContent.length() > 500) {
            safeContent = safeContent.substring(0, 500);
        }

        // 获取用户信息
        SysUser user = loginDao.selectById(userId);
        String nickname = getNickname(user);

        // 保存消息到数据库
        ChatMessage saved = saveMessage(safeContent, userId, nickname, messageType);
        if (saved == null) {
            dispatcher.sendError(session, "消息发送失败，请稍后重试");
            return;
        }

        // 构建广播消息
        Map<String, Object> chatData = new HashMap<>();
        // 使用数据库生成的消息ID，确保前后端可去重/对齐
        chatData.put("id", saved.getId());
        chatData.put("senderId", userId);
        chatData.put("senderName", nickname);
        chatData.put("content", safeContent);
        chatData.put("type", messageType);
        chatData.put("createTime", saved.getCreateTime());

        // 广播给所有在线用户
        dispatcher.broadcast("chat", chatData);

        // 发送消息确认给发送者
        if (messageId != null) {
            Map<String, Object> ackData = new HashMap<>();
            ackData.put("messageId", messageId);
            ackData.put("status", "success");
            ackData.put("serverMessageId", chatData.get("id"));
            dispatcher.sendToUser(userId, "ack", ackData);
        }
    }

    /**
     * 处理心跳消息
     * 回复心跳确认
     */
    private void handleHeartbeat(WebSocketSession session, String messageId) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId == null) return;

        // 回复心跳
        if (messageId != null) {
            Map<String, Object> ackData = new HashMap<>();
            ackData.put("messageId", messageId);
            ackData.put("status", "pong");
            ackData.put("timestamp", System.currentTimeMillis());
            dispatcher.sendToUser(userId, "heartbeat", ackData);
        }
    }

    /**
     * 处理输入状态消息
     * 广播"正在输入"状态给其他用户
     */
    private void handleTypingMessage(WebSocketSession session, JsonNode data) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId == null) return;

        SysUser user = loginDao.selectById(userId);
        String nickname = getNickname(user);

        boolean isTyping = data.has("isTyping") && data.get("isTyping").asBoolean();

        // 广播输入状态给其他用户（不通知自己）
        Map<String, Object> typingData = new HashMap<>();
        typingData.put("userId", userId);
        typingData.put("nickname", nickname);
        typingData.put("isTyping", isTyping);

        Set<String> onlineUsers = SessionManager.getOnlineUserIds();
        for (String uid : onlineUsers) {
            if (!uid.equals(userId)) {
                dispatcher.sendToUser(uid, "typing", typingData);
            }
        }
    }

    /**
     * 验证 Token
     * 从URL参数中获取token并验证
     */
    private String validateToken(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return null;

        String query = uri.getQuery();
        if (query == null || query.isEmpty()) return null;

        // 解析 token 参数
        String token = null;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && "token".equals(kv[0])) {
                try {
                    token = URLDecoder.decode(kv[1], "UTF-8");
                } catch (Exception e) {
                    token = kv[1];
                }
                break;
            }
        }

        if (token == null || token.isEmpty()) return null;

        // 验证 token
        SysUser user = TokenUtill.getSysUserFromToken(token);
        return user != null ? user.getId() : null;
    }

    /**
     * 更新用户在线状态
     */
    private void updateOnlineStatus(String userId, boolean online) {
        try {
            SysUser user = new SysUser();
            user.setId(userId);
            user.setOnlineStatus(online);
            user.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
            loginDao.updateInfo(user);
        } catch (Exception e) {
            log.error("更新用户在线状态失败: {}", e.getMessage());
        }
    }

    /**
     * 保存消息到数据库
     */
    private ChatMessage saveMessage(String content, String senderId, String senderName, String type) {
        try {
            ChatMessage message = new ChatMessage();
            message.setContent(content);
            message.setSenderId(senderId);
            message.setSenderName(senderName);
            message.setType(type);
            message.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
            chatMessageDao.insert(message);
            return message;
        } catch (Exception e) {
            log.error("保存消息失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取在线用户列表
     * 从SessionManager获取真正在线的用户
     */
    private List<Map<String, Object>> getOnlineUserList() {
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> onlineSessionUsers = SessionManager.getOnlineUserIds();

        for (String userId : onlineSessionUsers) {
            try {
                Map<String, Object> userInfo = onlineUserDao.selectOnlineUserById(userId);
                if (userInfo != null) {
                    result.add(userInfo);
                } else {
                    // 如果数据库查不到，创建基本信息
                    SysUser user = loginDao.selectById(userId);
                    if (user != null) {
                        Map<String, Object> info = getUserInfo(user);
                        result.add(info);
                    }
                }
            } catch (Exception e) {
                log.error("获取在线用户信息失败: userId={}", userId);
            }
        }
        return result;
    }

    /**
     * 获取用户信息Map
     */
    private Map<String, Object> getUserInfo(SysUser user) {
        Map<String, Object> info = new HashMap<>();
        if (user != null) {
            info.put("id", user.getId());
            info.put("nickname", getNickname(user));
            info.put("avatar", user.getAvatar());
            info.put("username", user.getUsername());
        }
        return info;
    }

    /**
     * 获取用户昵称，为空时返回用户名或默认值
     */
    private String getNickname(SysUser user) {
        if (user == null) return "用户";
        if (user.getNickName() != null && !user.getNickName().isEmpty()) {
            return user.getNickName();
        }
        if (user.getUsername() != null) {
            return user.getUsername();
        }
        return "用户" + user.getId();
    }
}
