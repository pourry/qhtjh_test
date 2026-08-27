package com.example.spring_boot_mode.model.web.messageCenter;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.model.dao.messageCenter.ChatMessageDao;
import com.example.spring_boot_mode.model.service.messageCenter.ChatMessageService;
import com.example.spring_boot_mode.model.service.messageCenter.ChatRateLimiter;
import com.example.spring_boot_mode.model.service.messageCenter.OnlineUserService;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.model.entity.messageCenter.ChatMessage;
import com.example.spring_boot_mode.utils.TokenUtill;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.websocket.MessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 聊天控制器
 * 提供聊天室相关的REST API接口
 * 支持WebSocket和HTTP两种方式发送消息
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatMessageDao chatMessageDao;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @Autowired
    private ChatRateLimiter chatRateLimiter;

    /** 消息内容最大长度（与 Service 同步） */
    @Value("${chat.message.max-length:500}")
    private int maxLength;

    /**
     * 获取聊天消息列表
     * GET /chat/messages?limit=50
     */
    @GetMapping("/messages")
    public ResponseObjectEntity getMessages(
            @RequestParam(defaultValue = "50") int limit,
            HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return chatMessageService.getChatMessages(limit);
    }

    /**
     * 获取最近 N 条聊天消息（Top 浮层预览）
     * GET /chat/recent?limit=5
     */
    @GetMapping("/recent")
    public ResponseObjectEntity getRecentMessages(
            @RequestParam(required = false, defaultValue = "5") int limit,
            HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return chatMessageService.getRecentChatMessages(limit);
    }

    /**
     * 分页获取聊天消息（加载更多历史消息）
     * GET /chat/messages/page?lastId=100&limit=50
     *
     * @param lastId 当前已加载的最早消息ID，不传则从最新开始
     * @param limit  每页数量，默认50
     */
    @GetMapping("/messages/page")
    public ResponseObjectEntity getMessagesByPage(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "50") int limit,
            HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return chatMessageService.getChatMessagesByPage(lastId, limit);
    }

    /**
     * 关键字搜索消息
     * GET /chat/messages/search?keyword=xxx&limit=50
     */
    @GetMapping("/messages/search")
    public ResponseObjectEntity searchMessages(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "50") Integer limit,
            HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return chatMessageService.searchMessages(keyword, limit);
    }

    /**
     * 获取消息总数
     * GET /chat/messages/count
     */
    @GetMapping("/messages/count")
    public ResponseObjectEntity getMessageCount(HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return chatMessageService.getTotalCount();
    }

    /**
     * 发送聊天消息（HTTP方式，同时通过WebSocket广播）
     * POST /chat/sendMessage
     */
    @PostMapping("/sendMessage")
    public ResponseObjectEntity sendMessage(
            @RequestParam String content,
            HttpServletRequest request) {
        SysUser currentUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(currentUser)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }

        // 验证消息内容
        if (content == null || content.trim().isEmpty()) {
            return ResponseUtil.error("消息内容不能为空");
        }
        if (content.length() > maxLength) {
            return ResponseUtil.error("消息内容不能超过" + maxLength + "字符");
        }

        // 限流（HTTP 路径同样要限流，避免绕过 WS 的限流逻辑）
        if (!chatRateLimiter.tryAcquire(currentUser.getId())) {
            return ResponseUtil.error("发送过于频繁，请稍后再试");
        }

        // 获取用户完整信息
        SysUser userInfo = loginDao.selectById(currentUser.getId());
        String nickname = getNickname(userInfo, currentUser.getId());

        // 保存消息到数据库
        ChatMessage message = new ChatMessage();
        String safeContent = com.example.spring_boot_mode.utils.ChatContentSanitizer.sanitize(content.trim());
        if (safeContent.isEmpty()) {
            return ResponseUtil.error("消息内容不能为空");
        }
        if (safeContent.length() > maxLength) {
            safeContent = safeContent.substring(0, maxLength);
        }
        message.setContent(safeContent);
        message.setSenderId(currentUser.getId());
        message.setSenderName(nickname);
        message.setType("text");
        message.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
        chatMessageDao.insert(message);

        // 通过WebSocket广播消息给所有在线用户
        Map<String, Object> chatData = new HashMap<>();
        // 使用数据库生成的消息ID，确保前后端可去重/对齐
        chatData.put("id", message.getId());
        chatData.put("senderId", currentUser.getId());
        chatData.put("senderName", nickname);
        chatData.put("content", safeContent);
        chatData.put("type", "text");
        chatData.put("createTime", message.getCreateTime());
        messageDispatcher.broadcast("chat", chatData);

        // 返回成功
        Map<String, Object> result = new HashMap<>();
        result.put("id", message.getId());
        result.put("content", safeContent);
        result.put("senderId", currentUser.getId());
        result.put("senderName", nickname);
        result.put("createTime", message.getCreateTime());

        return ResponseUtil.success(result);
    }

    /**
     * 删除聊天消息（仅允许删除自己的消息）
     * DELETE /chat/messages/{id}
     */
    @DeleteMapping("/messages/{id}")
    public ResponseObjectEntity deleteMessage(
            @PathVariable Long id,
            HttpServletRequest request) {
        SysUser currentUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(currentUser)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }

        ResponseObjectEntity result = chatMessageService.deleteMessage(id, currentUser.getId());

        // 如果删除成功，通过WebSocket广播删除事件
        if (result.getCode() == 200) {
            Map<String, Object> deleteData = new HashMap<>();
            deleteData.put("messageId", id);
            messageDispatcher.broadcast("messageDeleted", deleteData);
        }

        return result;
    }

    /**
     * 获取在线用户列表
     * GET /chat/onlineUsers
     */
    @GetMapping("/onlineUsers")
    public ResponseObjectEntity getOnlineUsers(HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return chatMessageService.getOnlineUsers();
    }

    /**
     * 获取当前真实在线用户列表（基于WebSocket连接状态）
     * GET /chat/realOnlineUsers
     */
    @GetMapping("/realOnlineUsers")
    public ResponseObjectEntity getRealOnlineUsers(HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        List<Map<String, Object>> onlineUsers = onlineUserService.getRealOnlineUserList();
        return ResponseUtil.success(onlineUsers);
    }

    /**
     * 获取用户昵称
     */
    private String getNickname(SysUser userInfo, String userId) {
        if (userInfo != null) {
            if (userInfo.getNickName() != null && !userInfo.getNickName().isEmpty()) {
                return userInfo.getNickName();
            }
            if (userInfo.getUsername() != null) {
                return userInfo.getUsername();
            }
        }
        return "用户" + userId;
    }
}
