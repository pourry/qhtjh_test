package com.example.spring_boot_mode.model.entity.messageCenter;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 通知实体类
 * 对应数据库表: notification
 */
@TableName("notification")
public class Notification {
    
    /** 通知ID */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    /** 通知标题 */
    private String title;
    
    /** 通知内容/描述 */
    private String description;
    
    /** 通知类型: info-信息, warning-警告, success-成功, announcement-公告 */
    private String type;
    
    /** 是否已读: false-未读, true-已读 */
    @com.baomidou.mybatisplus.annotation.TableField("is_read")
    @JsonProperty("isRead")
    private Boolean isRead;
    
    /** 接收者用户ID */
    private String receiverId;
    
    /** 批次ID（同一批发布的通知共享同一个批次ID） */
    private String batchId;
    
    /** 发布者用户ID */
    private String publisherId;
    
    /** 创建时间 */
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("isRead")
    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}