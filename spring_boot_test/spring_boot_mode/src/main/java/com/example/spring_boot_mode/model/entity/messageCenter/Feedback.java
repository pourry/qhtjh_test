package com.example.spring_boot_mode.model.entity.messageCenter;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 反馈实体类
 * 对应数据库表: feedback
 */
@TableName("feedback")
public class Feedback {
    
    /** 反馈ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 反馈类型: bug-Bug报告, feature-功能建议, improvement-改进建议, other-其他问题 */
    private String type;
    
    /** 反馈标题 */
    private String title;
    
    /** 反馈详细描述 */
    private String description;
    
    /** 联系方式(邮箱或手机号) */
    private String contact;
    
    /** 反馈状态: pending-待处理, processing-处理中, resolved-已解决, closed-已关闭 */
    private String status;
    
    /** 提交者用户ID */
    private String userId;
    
    /** 提交者用户名 */
    private String userName;
    
    /** 管理员回复内容 */
    private String reply;
    
    /** 处理者用户ID */
    private String handlerId;
    
    /** 创建时间 */
    private String createTime;
    
    /** 更新时间 */
    private String updateTime;
    
    /** 附件图片路径列表(JSON数组字符串) */
    private String images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getImages() {
        return images;
    }
    
    public void setImages(String images) {
        this.images = images;
    }
}