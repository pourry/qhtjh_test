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
        // 幂等检查：同一个 target_type + target_id + user_id，同时只能有一条 is_open=1 的记录
        // 先查有没有正在生效的（不限 status，pending/triggered 都算），避免并发重复插入
        Reminder existing = reminderDao.selectOneByCondition(
                reminder.getTargetType(),
                reminder.getTargetId(),
                reminder.getUserId(),
                1, null); // is_open=1, status 不限

        if (existing != null) {
            // 已有生效记录 → 复用它的 ID，然后重置状态
            reminder.setId(existing.getId());
        } else {
            // 没有生效记录 → 再查有没有 is_open=0 的历史记录，也复用
            Reminder oldRecord = reminderDao.selectOneByCondition(
                    reminder.getTargetType(),
                    reminder.getTargetId(),
                    reminder.getUserId(),
                    0, null);
            if (oldRecord != null) {
                reminder.setId(oldRecord.getId());
            }
        }

        if (reminder.getId() != null) {
            // 更新：强制重置为 pending 状态
            reminder.setStatus(Reminder.STATUS_PENDING);
            reminder.setTriggerTime(null);
            reminder.setIsRead(0);
            reminder.setIsOpen(1);
            reminder.setUpdateTime(LocalDateTime.now());
            setupNextRemindTime(reminder);
            reminderDao.updateById(reminder);
            return reminder;
        }

        // 真的没有 → 新建
        initNewReminderFields(reminder);
        reminderDao.insert(reminder);
        return reminder;
    }

    private void initNewReminderFields(Reminder reminder) {
        if (reminder.getStatus() == null) {
            reminder.setStatus(Reminder.STATUS_PENDING);
        }
        if (reminder.getIsOpen() == null) {
            reminder.setIsOpen(1);
        }
        if (reminder.getIsRead() == null) {
            reminder.setIsRead(0);
        }
        if (reminder.getRepeatType() == null || reminder.getRepeatType().isEmpty()) {
            reminder.setRepeatType(Reminder.REPEAT_NONE);
        }
        reminder.setCreateTime(LocalDateTime.now());
        reminder.setUpdateTime(LocalDateTime.now());
        setupNextRemindTime(reminder);
    }

    /**
     * 根据重复类型计算下次提醒时间
     */
    private void setupNextRemindTime(Reminder reminder) {
        LocalDateTime base = reminder.getNextRemindTime() != null
                ? reminder.getNextRemindTime()
                : reminder.getRemindTime();

        if (base == null) {
            base = LocalDateTime.now();
        }

        String repeatType = reminder.getRepeatType();
        if (repeatType == null || Reminder.REPEAT_NONE.equals(repeatType)) {
            reminder.setNextRemindTime(null);
            return;
        }

        LocalDateTime next;
        switch (repeatType) {
            case Reminder.REPEAT_HOURLY:
                next = base.plusHours(1);
                break;
            case Reminder.REPEAT_DAILY:
                next = base.plusDays(1);
                break;
            case Reminder.REPEAT_WEEKLY:
                next = base.plusWeeks(1);
                break;
            case Reminder.REPEAT_MONTHLY:
                next = base.plusMonths(1);
                break;
            case Reminder.REPEAT_YEARLY:
                next = base.plusYears(1);
                break;
            case Reminder.REPEAT_CUSTOM:
                int interval = reminder.getRepeatInterval() != null ? reminder.getRepeatInterval() : 60;
                next = base.plusMinutes(interval);
                break;
            default:
                next = null;
        }
        reminder.setNextRemindTime(next);
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
    public int getUnreadCount(String userId) {
        return reminderDao.countTriggeredUnread(userId);
    }

    @Override
    @Transactional
    public void markAsRead(String id) {
        reminderDao.markAsRead(id);
    }

    @Override
    @Transactional
    public void markAllAsRead(String userId) {
        reminderDao.markAllAsRead(userId);
    }

    @Override
    @Transactional
    public void checkAndTrigger() {
        LocalDateTime now = LocalDateTime.now();
        log.warn("===== 开始检查到期提醒, 当前时间: {} =====", now);

        List<Reminder> pendingList = reminderDao.selectPendingReminders(now);
        log.warn("查询到 {} 条待触发提醒（条件: status=pending, is_open=1, 时间<={}）", pendingList.size(), now);

        for (Reminder r : pendingList) {
            log.warn("  → 到期提醒: id={}, name={}, remind_time={}, next_remind_time={}, repeat_type={}",
                    r.getId(), r.getTargetName(), r.getRemindTime(), r.getNextRemindTime(), r.getRepeatType());
        }

        if (pendingList.isEmpty()) {
            log.warn("没有到期的提醒");
            return;
        }

        for (Reminder reminder : pendingList) {
            try {
                triggerReminder(reminder, now);
            } catch (Exception e) {
                log.error("触发提醒失败, id={}", reminder.getId(), e);
            }
        }
    }

    /**
     * 触发单条提醒
     * - 无论单次还是周期性，都把当前记录改成 triggered + is_read=0
     * - 周期性且还有下一次的，自动复制一条新的 pending 记录
     */
    private void triggerReminder(Reminder reminder, LocalDateTime now) {
        // 第一步：当前记录改为已触发（让用户能看到、能标记已读）
        // 注意：不要动 is_open！status='triggered' 已经让定时任务不会再查它，
        // 而且 getActiveReminder 需要 is_open=1 来识别活跃提醒
        reminder.setStatus(Reminder.STATUS_TRIGGERED);
        reminder.setTriggerTime(now);
        reminder.setIsRead(0);
        reminder.setUpdateTime(now);
        reminderDao.updateById(reminder);
        log.info("提醒已触发: id={}, targetName={}", reminder.getId(), reminder.getTargetName());

        // 第二步：周期性提醒 → 自动复制一条新的 pending 记录
        if (shouldCreateNextCycle(reminder)) {
            Reminder next = buildNextCycleRecord(reminder, now);
            reminderDao.insert(next);
            log.info("周期性提醒已续期: 新id={}, 下次={}", next.getId(), next.getRemindTime());
        }
    }

    /**
     * 判断是否应该创建下一个周期的提醒
     */
    private boolean shouldCreateNextCycle(Reminder reminder) {
        String repeatType = reminder.getRepeatType();
        if (repeatType == null || Reminder.REPEAT_NONE.equals(repeatType)) {
            return false;
        }
        // 设置了结束时间且已到 → 不再续期
        if (reminder.getRepeatEndTime() != null
                && LocalDateTime.now().isAfter(reminder.getRepeatEndTime())) {
            return false;
        }
        return true;
    }

    /**
     * 基于已触发的记录，创建下一个周期的新记录
     */
    private Reminder buildNextCycleRecord(Reminder old, LocalDateTime now) {
        Reminder next = new Reminder();
        // 复制业务字段
        next.setTargetType(old.getTargetType());
        next.setTargetId(old.getTargetId());
        next.setTargetName(old.getTargetName());
        next.setAlias(old.getAlias());
        next.setAddress(old.getAddress());
        next.setUserId(old.getUserId());
        next.setRemindMsg(old.getRemindMsg());
        next.setRepeatType(old.getRepeatType());
        next.setRepeatInterval(old.getRepeatInterval());
        next.setRepeatEndTime(old.getRepeatEndTime());
        // 新记录状态：待触发、未读、开启
        next.setStatus(Reminder.STATUS_PENDING);
        next.setIsOpen(1);
        next.setIsRead(0);
        next.setTriggerTime(null);
        // 计算下一次提醒时间，设为新的 remindTime（不设 nextRemindTime，让定时任务只用 remindTime）
        LocalDateTime nextTime = calcNextTime(old);
        next.setRemindTime(nextTime);
        next.setNextRemindTime(null);
        next.setCreateTime(now);
        next.setUpdateTime(now);
        return next;
    }

    /**
     * 根据重复类型计算下一次提醒时间（基于本次触发的时间点）
     */
    private LocalDateTime calcNextTime(Reminder old) {
        LocalDateTime base = old.getNextRemindTime() != null
                ? old.getNextRemindTime()
                : old.getRemindTime();
        if (base == null) {
            base = LocalDateTime.now();
        }
        String repeatType = old.getRepeatType();
        switch (repeatType) {
            case Reminder.REPEAT_HOURLY:   return base.plusHours(1);
            case Reminder.REPEAT_DAILY:    return base.plusDays(1);
            case Reminder.REPEAT_WEEKLY:   return base.plusWeeks(1);
            case Reminder.REPEAT_MONTHLY:  return base.plusMonths(1);
            case Reminder.REPEAT_YEARLY:   return base.plusYears(1);
            case Reminder.REPEAT_CUSTOM:
                int interval = old.getRepeatInterval() != null ? old.getRepeatInterval() : 60;
                return base.plusMinutes(interval);
            default:
                return null;
        }
    }

    @Override
    public Reminder getByTarget(String targetType, String targetId, String userId) {
        return reminderDao.selectByTarget(targetType, targetId, userId);
    }

    @Override
    public Reminder getActiveReminder(String targetType, String targetId, String userId) {
        // 查 is_open=1 的最新一条（不限 status），这样 pending 和 triggered 都能找到
        return reminderDao.selectOneByCondition(targetType, targetId, userId, 1, null);
    }

    @Override
    public List<Reminder> getAllReminders() {
        return reminderDao.selectAll();
    }
}
