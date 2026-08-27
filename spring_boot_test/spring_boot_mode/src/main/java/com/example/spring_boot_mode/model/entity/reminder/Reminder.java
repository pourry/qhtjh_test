package com.example.spring_boot_mode.model.entity.reminder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 提醒实体类
 * 用户自己给自己设置的提醒，与系统通知本质不同
 */
@Data
@TableName("reminder")
public class Reminder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 目标类型：animation-动画, comic-漫画, novel-小说, game-游戏
     */
    private String targetType;

    /**
     * 目标ID（animation/comic/novel/game的主键）
     */
    private String targetId;

    /**
     * 目标名称（冗余字段，方便展示）
     */
    private String targetName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 提醒时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime remindTime;

    /**
     * 自定义提醒消息
     */
    private String remindMsg;

    /**
     * 状态：pending-待触发, triggered-已触发, cancelled-已取消
     */
    private String status;

    /**
     * 实际触发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime triggerTime;

    /**
     * 是否开启：0-关闭, 1-开启
     */
    @TableField("is_open")
    private Integer isOpen;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 状态常量
     */
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_TRIGGERED = "triggered";
    public static final String STATUS_CANCELLED = "cancelled";

    /**
     * 目标类型常量
     */
    public static final String TYPE_ANIMATION = "animation";
    public static final String TYPE_COMIC = "comic";
    public static final String TYPE_NOVEL = "novel";
    public static final String TYPE_GAME = "game";
}
