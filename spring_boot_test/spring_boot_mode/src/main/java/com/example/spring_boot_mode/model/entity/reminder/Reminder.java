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
     * 目标别名（冗余字段，方便展示）
     */
    private String alias;

    /**
     * 目标地址（冗余字段，方便展示）
     */
    private String address;

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

    /**
     * 是否已读：0-未读, 1-已读
     * 仅已触发的提醒需要区分已读/未读
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 重复类型：none-不重复, hourly-每小时, daily-每天, weekly-每周, monthly-每月, yearly-每年, custom-自定义
     */
    @TableField("repeat_type")
    private String repeatType;

    /**
     * 自定义重复间隔（分钟），仅当repeatType=custom时有效
     */
    @TableField("repeat_interval")
    private Integer repeatInterval;

    /**
     * 下次提醒时间（周期性提醒触发后自动计算下一次）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("next_remind_time")
    private LocalDateTime nextRemindTime;

    /**
     * 周期结束时间（为空表示无限重复）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("repeat_end_time")
    private LocalDateTime repeatEndTime;

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

    /**
     * 重复类型常量
     */
    public static final String REPEAT_NONE = "none";
    public static final String REPEAT_HOURLY = "hourly";
    public static final String REPEAT_DAILY = "daily";
    public static final String REPEAT_WEEKLY = "weekly";
    public static final String REPEAT_MONTHLY = "monthly";
    public static final String REPEAT_YEARLY = "yearly";
    public static final String REPEAT_CUSTOM = "custom";
}
