package com.example.spring_boot_mode.model.service.reminder;

import com.example.spring_boot_mode.model.entity.reminder.Reminder;

import java.util.List;

/**
 * 提醒服务接口
 */
public interface ReminderService {

    /**
     * 创建或更新提醒
     */
    Reminder saveReminder(Reminder reminder);

    /**
     * 取消提醒
     */
    void cancelReminder(String id);

    /**
     * 删除提醒
     */
    void deleteReminder(String id);

    /**
     * 根据ID获取提醒
     */
    Reminder getById(String id);

    /**
     * 分页获取我的提醒列表
     */
    List<Reminder> getMyReminders(String userId, String status, String targetType,
                                   String keyword, int page, int size);

    /**
     * 获取我的提醒总数
     */
    int getMyRemindersCount(String userId, String status, String targetType, String keyword);

    /**
     * 查询并触发到期的提醒（定时任务调用）
     */
    void checkAndTrigger();

    /**
     * 根据目标信息获取提醒
     */
    Reminder getByTarget(String targetType, String targetId, String userId);

    /**
     * 获取指定类型和ID的所有开启提醒
     */
    Reminder getActiveReminder(String targetType, String targetId, String userId);

    List<Reminder> getAllReminders();

    /**
     * 获取某用户已触发但未读的提醒数量
     */
    int getUnreadCount(String userId);

    /**
     * 标记单条提醒为已读
     */
    void markAsRead(String id);

    /**
     * 标记某用户所有已触发提醒为已读
     */
    void markAllAsRead(String userId);
}
