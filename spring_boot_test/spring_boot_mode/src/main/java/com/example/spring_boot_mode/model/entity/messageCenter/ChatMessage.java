package com.example.spring_boot_mode.model.entity.messageCenter;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 聊天消息实体类
 * 对应数据库表: chat_message
 */
@TableName("chat_message")
public class ChatMessage {
    
    /** 消息ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 发送者用户ID */
    private String senderId;
    
    /** 发送者昵称 */
    private String senderName;
    
    /** 消息内容 */
    private String content;
    
    /** 消息类型: text-文本, system-系统消息 */
    private String type;
    
    /** 创建时间 */
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}