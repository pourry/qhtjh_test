package com.example.spring_boot_mode.model.web.messageCenter;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.model.service.messageCenter.NotificationService;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.utils.TokenUtill;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 通知控制器
 * 提供通知相关的REST API接口
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private LoginDao loginDao;
    
    /**
     * 获取当前用户的通知列表
     * GET /notification/list
     */
    @GetMapping("/list")
    public ResponseObjectEntity getNotifications(HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.getNotifications(user.getId());
    }
    
    /**
     * 获取未读通知数量
     * GET /notification/unreadCount
     */
    @GetMapping("/unreadCount")
    public ResponseObjectEntity getUnreadCount(HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.getUnreadCount(user.getId());
    }
    
    /**
     * 标记通知为已读
     * POST /notification/markAsRead
     */
    @PostMapping("/markAsRead")
    public ResponseObjectEntity markAsRead(
            @RequestParam Long id,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.markAsRead(id, user.getId());
    }
    
    /**
     * 标记所有通知为已读
     * POST /notification/markAllAsRead
     */
    @PostMapping("/markAllAsRead")
    public ResponseObjectEntity markAllAsRead(HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.markAllAsRead(user.getId());
    }
    
    /**
     * 发布通知（管理员功能）
     * POST /notification/publish
     */
    @PostMapping("/publish")
    public ResponseObjectEntity publishNotification(
            @RequestBody Map<String, String> data,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        String title = data.get("title");
        String description = data.get("description");
        String type = data.getOrDefault("type", "info");
        String scope = data.getOrDefault("scope", "all");
        String receiverIds = data.get("receiverIds");
        return notificationService.publishNotification(title, description, type, 
                user.getId(), receiverIds, scope);
    }
    
    /**
     * 删除通知
     * POST /notification/delete
     */
    @PostMapping("/delete")
    public ResponseObjectEntity deleteNotification(
            @RequestParam Long id,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.deleteNotification(id, user.getId());
    }
    
    /**
     * 获取所有用户列表（用于发布通知时选择接收者）
     * GET /notification/allUsers
     */
    @GetMapping("/allUsers")
    public ResponseObjectEntity getAllUsers(HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        // 使用原生 SQL 查询所有用户
        List<Map<String, Object>> result = loginDao.selectAllUsersForNotification();
        return ResponseUtil.success(result);
    }
    
    /**
     * 获取当前用户发布的通知列表
     * GET /notification/publishedList
     */
    @GetMapping("/publishedList")
    public ResponseObjectEntity getPublishedNotifications(HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.getPublishedNotifications(user.getId());
    }
    
    /**
     * 获取当前用户发布的通知列表（支持分页）
     * GET /notification/publishedListPage
     */
    @GetMapping("/publishedListPage")
    public ResponseObjectEntity getPublishedNotificationsPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.getPublishedNotificationsPaged(user.getId(), page, size);
    }
    
    /**
     * 删除发布的通知
     * POST /notification/deletePublished
     */
    @PostMapping("/deletePublished")
    public ResponseObjectEntity deletePublishedNotification(
            @RequestParam Long id,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.deletePublishedNotification(id, user.getId());
    }

    /**
     * 获取当前用户发布的批次通知列表（支持分页，按批次聚合）
     * GET /notification/publishedBatchesPage
     */
    @GetMapping("/publishedBatchesPage")
    public ResponseObjectEntity getPublishedBatchesPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.getPublishedBatchesPaged(user.getId(), page, size);
    }

    /**
     * 获取批次的接收者详情
     * GET /notification/batchReceivers
     */
    @GetMapping("/batchReceivers")
    public ResponseObjectEntity getBatchReceivers(
            @RequestParam String batchId,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.getBatchReceivers(batchId);
    }

    /**
     * 获取批次的接收者详情（支持分页）
     * GET /notification/batchReceiversPage
     */
    @GetMapping("/batchReceiversPage")
    public ResponseObjectEntity getBatchReceiversPaged(
            @RequestParam String batchId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.getBatchReceiversPaged(batchId, page, size);
    }

    /**
     * 删除发布的批次
     * POST /notification/deletePublishedBatch
     */
    @PostMapping("/deletePublishedBatch")
    public ResponseObjectEntity deletePublishedBatch(
            @RequestParam String batchId,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return notificationService.deletePublishedBatch(batchId, user.getId());
    }

    /**
     * 更新发布的批次内容
     * POST /notification/updatePublishedBatch
     */
    @PostMapping("/updatePublishedBatch")
    public ResponseObjectEntity updatePublishedBatch(
            @RequestBody Map<String, String> data,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        String batchId = data.get("batchId");
        String title = data.get("title");
        String description = data.get("description");
        String type = data.getOrDefault("type", "info");
        return notificationService.updatePublishedBatch(batchId, title, description, type, user.getId());
    }
}