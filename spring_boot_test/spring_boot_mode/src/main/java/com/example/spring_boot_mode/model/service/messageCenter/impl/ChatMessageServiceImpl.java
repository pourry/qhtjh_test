package com.example.spring_boot_mode.model.service.messageCenter.impl;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.messageCenter.ChatMessageDao;
import com.example.spring_boot_mode.model.dao.messageCenter.OnlineUserDao;
import com.example.spring_boot_mode.model.entity.messageCenter.ChatMessage;
import com.example.spring_boot_mode.model.service.messageCenter.ChatMessageService;
import com.example.spring_boot_mode.model.service.messageCenter.ChatRateLimiter;
import com.example.spring_boot_mode.utils.ChatContentSanitizer;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 聊天消息服务实现类
 */
@Slf4j
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageDao chatMessageDao;

    @Autowired
    private OnlineUserDao onlineUserDao;

    @Autowired
    private ChatRateLimiter chatRateLimiter;

    /** 消息列表默认加载数量 */
    private static final int DEFAULT_LIMIT = 50;
    /** 单次查询最大条数（防止前端恶意传大数） */
    private static final int MAX_LIMIT = 200;
    /** 消息内容最大长度 */
    @Value("${chat.message.max-length:500}")
    private int maxLength;
    /** 搜索关键字最大长度（防止 LIKE 全表超长扫描） */
    private static final int MAX_KEYWORD_LENGTH = 64;
    /** 系统消息内容最大长度（更长，便于公告） */
    private static final int MAX_SYSTEM_LENGTH = 2000;

    @Override
    public ResponseObjectEntity getChatMessages(int limit) {
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        }
        // 直接由 SQL 按 ID 升序返回，避免内存排序带来的开销和不稳定性
        List<ChatMessage> messages = chatMessageDao.selectLatestMessagesAsc(limit);
        return ResponseUtil.success(messages);
    }

    @Override
    public ResponseObjectEntity getRecentChatMessages(int limit) {
        if (limit <= 0) limit = 5;
        if (limit > 30) limit = 30;
        List<ChatMessage> messages = chatMessageDao.selectLatestMessagesAsc(limit);
        return ResponseUtil.success(messages);
    }

    @Override
    public ResponseObjectEntity getChatMessagesByPage(Long lastId, int limit) {
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        }
        // 多加载一条判断是否还有更多
        List<ChatMessage> messages = chatMessageDao.selectPageMessages(lastId, limit + 1);

        boolean hasMore = messages.size() > limit;
        if (hasMore) {
            messages = messages.subList(0, limit);
        }

        // SQL 是 id DESC，这里在内存里翻成 id ASC（更符合前端渲染习惯：旧→新）
        List<ChatMessage> result = messages.stream()
                .sorted(Comparator.comparing(ChatMessage::getId))
                .collect(Collectors.toList());

        Map<String, Object> pageData = new HashMap<>();
        pageData.put("list", result);
        pageData.put("hasMore", hasMore);
        // 返回这批消息中"最早一条"的 id 作为下次向前翻页的游标
        pageData.put("lastId", result.isEmpty() ? null : result.get(0).getId());

        return ResponseUtil.success(pageData);
    }

    @Override
    public ResponseObjectEntity getTotalCount() {
        long count = chatMessageDao.selectTotalCount();
        return ResponseUtil.success(count);
    }

    @Override
    public ResponseObjectEntity sendChatMessage(String content, String senderId, String senderName) {
        // 1. 入参校验
        if (content == null || content.trim().isEmpty()) {
            return ResponseUtil.error("消息内容不能为空");
        }
        if (content.length() > maxLength) {
            return ResponseUtil.error("消息内容不能超过" + maxLength + "字符");
        }

        // 2. 限流：每用户每秒/每分钟
        if (!chatRateLimiter.tryAcquire(senderId)) {
            return ResponseUtil.error("发送过于频繁，请稍后再试");
        }

        // 3. XSS/输入清洗：去除危险标签、控制字符等
        String safeContent = ChatContentSanitizer.sanitize(content);
        if (safeContent.isEmpty()) {
            return ResponseUtil.error("消息内容不能为空");
        }
        if (safeContent.length() > maxLength) {
            safeContent = safeContent.substring(0, maxLength);
        }

        // 4. 持久化
        ChatMessage message = new ChatMessage();
        message.setContent(safeContent);
        message.setSenderId(senderId);
        message.setSenderName(senderName);
        message.setType("text");
        message.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));

        try {
            int result = chatMessageDao.insert(message);
            if (result > 0) {
                return ResponseUtil.success(message);
            }
            return ResponseUtil.error("发送失败");
        } catch (Exception e) {
            log.error("保存聊天消息失败", e);
            return ResponseUtil.error("发送失败，请稍后重试");
        }
    }

    @Override
    public ResponseObjectEntity getOnlineUsers() {
        // 根据用户登录状态查询在线用户
        List<Map<String, Object>> onlineUsers = onlineUserDao.selectOnlineUsers();
        return ResponseUtil.success(onlineUsers);
    }

    @Override
    public ResponseObjectEntity deleteMessage(Long messageId, String currentUserId) {
        if (messageId == null) {
            return ResponseUtil.error("消息ID不能为空");
        }

        ChatMessage message = chatMessageDao.selectById(messageId);
        if (message == null) {
            // 已不存在，幂等返回成功（前端可能已经标记了 deleted 状态）
            return ResponseUtil.success("消息已删除");
        }

        // 权限校验：只能删除自己发送的消息
        if (!Objects.equals(message.getSenderId(), currentUserId)) {
            return ResponseUtil.error("无权删除此消息");
        }

        try {
            int result = chatMessageDao.deleteById(messageId);
            if (result > 0) {
                return ResponseUtil.success("删除成功");
            }
            return ResponseUtil.error("删除失败");
        } catch (Exception e) {
            log.error("删除消息失败: id={}", messageId, e);
            return ResponseUtil.error("删除失败");
        }
    }

    @Override
    public ResponseObjectEntity searchMessages(String keyword, Integer limit) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseUtil.error("搜索关键字不能为空");
        }
        if (keyword.length() > MAX_KEYWORD_LENGTH) {
            return ResponseUtil.error("搜索关键字过长");
        }
        int l = (limit == null || limit <= 0) ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);

        // 关键字也做一次清洗，避免 LIKE 注入或特殊字符
        String safe = ChatContentSanitizer.sanitize(keyword).trim();
        if (safe.isEmpty()) {
            return ResponseUtil.error("搜索关键字不能为空");
        }

        List<ChatMessage> hits = chatMessageDao.searchByContent(safe, l);
        // 按 id 倒序已由 SQL 完成，转换为前端友好字段
        List<Map<String, Object>> result = hits.stream().map(this::toSearchItem).collect(Collectors.toList());

        Map<String, Object> pageData = new HashMap<>();
        pageData.put("list", result);
        pageData.put("total", result.size());
        pageData.put("keyword", safe);
        return ResponseUtil.success(pageData);
    }

    @Override
    public int cleanMessagesBefore(String cutoffTime) {
        if (cutoffTime == null || cutoffTime.isEmpty()) {
            return 0;
        }
        try {
            int deleted = chatMessageDao.deleteBefore(cutoffTime);
            if (deleted > 0) {
                log.info("聊天室定时清理：删除 {} 条早于 {} 的消息", deleted, cutoffTime);
            }
            return deleted;
        } catch (Exception e) {
            log.error("聊天室定时清理失败: cutoffTime={}", cutoffTime, e);
            return 0;
        }
    }

    private Map<String, Object> toSearchItem(ChatMessage m) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", m.getId());
        item.put("senderId", m.getSenderId());
        item.put("senderName", m.getSenderName());
        item.put("content", m.getContent());
        item.put("type", m.getType());
        item.put("createTime", m.getCreateTime());
        return item;
    }
}
