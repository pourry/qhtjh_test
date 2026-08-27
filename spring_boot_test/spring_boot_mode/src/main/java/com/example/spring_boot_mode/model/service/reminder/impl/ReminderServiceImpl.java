package com.example.spring_boot_mode.model.service.reminder.impl;

import com.example.spring_boot_mode.model.dao.reminder.ReminderDao;
import com.example.spring_boot_mode.model.entity.reminder.Reminder;
import com.example.spring_boot_mode.model.service.reminder.ReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒服务实现类
 */
@Slf4j
@Service
public class ReminderServiceImpl implements ReminderService {

    @Autowired
    private ReminderDao reminderDao;

    @Override
    @Transactional
    public Reminder saveReminder(Reminder reminder) {
        if (reminder.getId() == null || reminder.getId().isEmpty()) {
            // 新增
            if (reminder.getStatus() == null) {
                reminder.setStatus(Reminder.STATUS_PENDING);
            }
            if (reminder.getIsOpen() == null) {
                reminder.setIsOpen(1);
            }
            reminder.setCreateTime(LocalDateTime.now());
            reminder.setUpdateTime(LocalDateTime.now());
            reminderDao.insert(reminder);
        } else {
            // 更新
            reminder.setUpdateTime(LocalDateTime.now());
            reminderDao.updateById(reminder);
        }
        return reminder;
    }

    @Override
    @Transactional
    public void cancelReminder(String id) {
        Reminder reminder = reminderDao.selectById(id);
        if (reminder != null) {
            reminder.setStatus(Reminder.STATUS_CANCELLED);
            reminder.setIsOpen(0);
            reminder.setUpdateTime(LocalDateTime.now());
            reminderDao.updateById(reminder);
        }
    }

    @Override
    @Transactional
    public void deleteReminder(String id) {
        reminderDao.deleteById(id);
    }

    @Override
    public Reminder getById(String id) {
        return reminderDao.selectById(id);
    }

    @Override
    public List<Reminder> getMyReminders(String userId, String status, String targetType,
                                          String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return reminderDao.selectMyList(userId, status, targetType, keyword, offset, size);
    }

    @Override
    public int getMyRemindersCount(String userId, String status, String targetType, String keyword) {
        return reminderDao.selectMyListTotal(userId, status, targetType, keyword);
    }

    @Override
    @Transactional
    public void checkAndTrigger() {
        LocalDateTime now = LocalDateTime.now();
        log.info("开始检查到期提醒, 当前时间: {}", now);

        // 查询所有待触发且已到时间的提醒
        List<Reminder> pendingList = reminderDao.selectPendingReminders(now);

        if (pendingList.isEmpty()) {
            log.info("没有到期的提醒");
            return;
        }

        log.info("发现 {} 条到期提醒", pendingList.size());

        for (Reminder reminder : pendingList) {
            try {
                triggerReminder(reminder);
            } catch (Exception e) {
                log.error("触发提醒失败, id={}", reminder.getId(), e);
            }
        }
    }

    /**
     * 触发单条提醒
     */
    private void triggerReminder(Reminder reminder) {
        reminder.setStatus(Reminder.STATUS_TRIGGERED);
        reminder.setIsOpen(0);
        reminder.setTriggerTime(LocalDateTime.now());
        reminder.setUpdateTime(LocalDateTime.now());
        reminderDao.updateById(reminder);

        log.info("提醒已触发: id={}, targetName={}", reminder.getId(), reminder.getTargetName());
    }

    @Override
    public Reminder getByTarget(String targetType, String targetId, String userId) {
        return reminderDao.selectByTarget(targetType, targetId, userId);
    }

    @Override
    public Reminder getActiveReminder(String targetType, String targetId, String userId) {
        return reminderDao.selectOneByCondition(targetType, targetId, userId, 1, Reminder.STATUS_PENDING);
    }
}
