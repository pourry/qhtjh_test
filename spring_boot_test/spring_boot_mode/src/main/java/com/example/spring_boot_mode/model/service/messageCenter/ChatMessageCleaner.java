package com.example.spring_boot_mode.model.service.messageCenter;

import com.example.spring_boot_mode.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 聊天室消息清理任务
 * <p>
 * 每天凌晨 3 点执行一次，删除 N 天前的消息，防止 chat_message 无限增长。
 * 保留天数可通过 chat.cleanup.retention-days 调整（默认 30 天）。
 *
 * @author mavis
 */
@Slf4j
@Component
public class ChatMessageCleaner {

    @Autowired
    private ChatMessageService chatMessageService;

    /** 消息保留天数 */
    @Value("${chat.cleanup.retention-days:30}")
    private int retentionDays;

    /** 清理任务启用开关（默认开启） */
    @Value("${chat.cleanup.enabled:true}")
    private boolean enabled;

    /**
     * 每天凌晨 3:00 执行
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void clean() {
        if (!enabled) {
            return;
        }
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -retentionDays);
            String cutoff = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", cal.getTime());
            int deleted = chatMessageService.cleanMessagesBefore(cutoff);
            if (deleted > 0) {
                log.info("【聊天室清理】删除 {} 条 {} 之前的消息", deleted, cutoff);
            } else {
                log.debug("【聊天室清理】无需清理（保留 {} 天）", retentionDays);
            }
        } catch (Exception e) {
            log.error("【聊天室清理】执行异常", e);
        }
    }
}
