package com.example.spring_boot_mode.model.web.reminder;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.reminder.Reminder;
import com.example.spring_boot_mode.model.service.reminder.ReminderService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提醒控制器
 * 独立于通知系统，用于管理用户自己设置的提醒
 */
@RestController
@RequestMapping("/reminder")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    /**
     * 创建或更新提醒
     * 如果传入的id为空，则创建新提醒；否则更新已有提醒
     */
    @PostMapping("/save")
    public ResponseObjectEntity save(@RequestBody Reminder reminder) {
        if (reminder.getUserId() == null || reminder.getUserId().isEmpty()) {
            return ResponseUtil.error("用户ID不能为空");
        }
        if (reminder.getTargetType() == null || reminder.getTargetType().isEmpty()) {
            return ResponseUtil.error("目标类型不能为空");
        }
        if (reminder.getTargetId() == null || reminder.getTargetId().isEmpty()) {
            return ResponseUtil.error("目标ID不能为空");
        }
        Reminder saved = reminderService.saveReminder(reminder);
        return ResponseUtil.success(saved);
    }

    /**
     * 取消提醒
     */
    @PostMapping("/cancel/{id}")
    public ResponseObjectEntity cancel(@PathVariable String id) {
        reminderService.cancelReminder(id);
        return ResponseUtil.success(null);
    }

    /**
     * 删除提醒
     */
    @DeleteMapping("/delete/{id}")
    public ResponseObjectEntity delete(@PathVariable String id) {
        reminderService.deleteReminder(id);
        return ResponseUtil.success(null);
    }

    /**
     * 根据ID获取提醒详情
     */
    @GetMapping("/detail/{id}")
    public ResponseObjectEntity detail(@PathVariable String id) {
        Reminder reminder = reminderService.getById(id);
        if (reminder == null) {
            return ResponseUtil.error("提醒不存在");
        }
        return ResponseUtil.success(reminder);
    }

    /**
     * 获取我的提醒列表（分页）
     */
    @GetMapping("/myList")
    public ResponseObjectEntity myList(
            @RequestParam("userId") String userId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "targetType", required = false) String targetType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<Reminder> list = reminderService.getMyReminders(userId, status, targetType, keyword, page, size);
        int total = reminderService.getMyRemindersCount(userId, status, targetType, keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return ResponseUtil.success(result);
    }

    /**
     * 根据目标信息查询提醒
     */
    @GetMapping("/byTarget")
    public ResponseObjectEntity byTarget(
            @RequestParam("targetType") String targetType,
            @RequestParam("targetId") String targetId,
            @RequestParam("userId") String userId) {
        Reminder reminder = reminderService.getByTarget(targetType, targetId, userId);
        return ResponseUtil.success(reminder);
    }

    /**
     * 获取指定目标的活跃提醒
     */
    @GetMapping("/activeByTarget")
    public ResponseObjectEntity activeByTarget(
            @RequestParam("targetType") String targetType,
            @RequestParam("targetId") String targetId,
            @RequestParam("userId") String userId) {
        Reminder reminder = reminderService.getActiveReminder(targetType, targetId, userId);
        return ResponseUtil.success(reminder);
    }

    /**
     * 手动触发检查（测试用）
     */
    @PostMapping("/check")
    public ResponseObjectEntity check() {
        reminderService.checkAndTrigger();
        return ResponseUtil.success(null);
    }
}
