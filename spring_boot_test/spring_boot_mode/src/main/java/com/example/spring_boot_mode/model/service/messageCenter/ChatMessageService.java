package com.example.spring_boot_mode.model.service.messageCenter;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;

/**
 * 聊天消息服务接口
 */
public interface ChatMessageService {

    /**
     * 获取聊天消息列表（最新的N条）
     *
     * @param limit 限制数量
     * @return 消息列表
     */
    ResponseObjectEntity getChatMessages(int limit);

    /**
     * 获取最近 N 条聊天消息（用于 Top 浮层预览），按时间正序返回
     */
    ResponseObjectEntity getRecentChatMessages(int limit);

    /**
     * 分页获取聊天消息（用于加载更多历史消息）
     *
     * @param lastId 当前已加载的最早消息ID
     * @param limit  每页数量
     * @return 分页结果（包含消息列表和是否还有更多）
     */
    ResponseObjectEntity getChatMessagesByPage(Long lastId, int limit);

    /**
     * 获取消息总数
     */
    ResponseObjectEntity getTotalCount();

    /**
     * 发送聊天消息
     *
     * @param content    消息内容
     * @param senderId   发送者ID
     * @param senderName 发送者昵称
     * @return 发送结果
     */
    ResponseObjectEntity sendChatMessage(String content, String senderId, String senderName);

    /**
     * 获取在线用户列表
     */
    ResponseObjectEntity getOnlineUsers();

    /**
     * 删除聊天消息（仅允许删除自己的消息）
     *
     * @param messageId     消息ID
     * @param currentUserId 当前用户ID
     */
    ResponseObjectEntity deleteMessage(Long messageId, String currentUserId);

    /**
     * 关键字搜索消息
     *
     * @param keyword 关键字（不可为空，最长 64）
     * @param limit   最大条数（1~200，默认 50）
     */
    ResponseObjectEntity searchMessages(String keyword, Integer limit);

    /**
     * 物理清理早于指定时间戳的消息（定时任务调用）
     *
     * @param cutoffTime yyyy-MM-dd HH:mm:ss 格式
     * @return 实际删除条数
     */
    int cleanMessagesBefore(String cutoffTime);
}
