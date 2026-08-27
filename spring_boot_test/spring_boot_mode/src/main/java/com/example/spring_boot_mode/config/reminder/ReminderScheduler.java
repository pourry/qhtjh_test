package com.example.spring_boot_mode.config.reminder;

import com.example.spring_boot_mode.model.service.reminder.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 提醒定时任务调度器
 * 每分钟检查一次到期提醒并触发
 */
@Slf4j
@Component
public class ReminderScheduler {

    @Autowired
    private ReminderService reminderService;

    /**
     * 每分钟执行一次检查
     */
    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        try {
            reminderService.checkAndTrigger();
        } catch (Exception e) {
            log.error("定时检查提醒发生异常", e);
        }
    }
}
