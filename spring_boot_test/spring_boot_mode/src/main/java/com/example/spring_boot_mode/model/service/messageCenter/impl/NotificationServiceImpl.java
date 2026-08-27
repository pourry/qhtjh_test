package com.example.spring_boot_mode.model.service.messageCenter.impl;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.model.dao.messageCenter.NotificationDao;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.model.entity.messageCenter.Notification;
import com.example.spring_boot_mode.model.service.messageCenter.NotificationService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.websocket.MessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 通知服务实现类
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationDao notificationDao;
    
    @Autowired
    private LoginDao loginDao;
    
    @Autowired
    private MessageDispatcher messageDispatcher;
    
    @Override
    public ResponseObjectEntity getNotifications(String receiverId) {
        if (receiverId == null || receiverId.isEmpty()) {
            return ResponseUtil.error("用户ID不能为空");
        }
        List<Notification> notifications = notificationDao.selectByReceiverId(receiverId);
        
        // 手动构建返回数据，确保字段名和类型正确
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Notification n : notifications) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", n.getId());
            item.put("title", n.getTitle());
            item.put("description", n.getDescription());
            item.put("type", n.getType());
            // 确保 isRead 是 Boolean 类型
            Boolean isRead = n.getIsRead();
            item.put("isRead", isRead != null ? isRead : false);
            item.put("receiverId", n.getReceiverId());
            item.put("batchId", n.getBatchId());
            item.put("publisherId", n.getPublisherId());
            item.put("createTime", n.getCreateTime());
            resultList.add(item);
            
            // 添加日志便于调试
            System.out.println("通知: id=" + n.getId() + ", title=" + n.getTitle() + ", isRead=" + n.getIsRead() + ", isReadClass=" + (n.getIsRead() != null ? n.getIsRead().getClass().getName() : "null"));
        }
        
        return ResponseUtil.success(resultList);
    }
    
    @Override
    public ResponseObjectEntity getUnreadCount(String receiverId) {
        if (receiverId == null || receiverId.isEmpty()) {
            return ResponseUtil.success(0);
        }
        int count = notificationDao.countUnreadByReceiverId(receiverId);
        return ResponseUtil.success(count);
    }
    
    @Override
    public ResponseObjectEntity markAsRead(Long id, String receiverId) {
        if (id == null) {
            return ResponseUtil.error("通知ID不能为空");
        }
        if (receiverId == null || receiverId.isEmpty()) {
            return ResponseUtil.error("用户ID不能为空");
        }
        
        // 添加日志便于调试
        System.out.println("markAsRead - id=" + id + ", receiverId=" + receiverId);
        
        // 先检查记录是否存在
        Notification notification = notificationDao.selectById(id);
        if (notification == null) {
            System.out.println("markAsRead - 通知不存在: id=" + id);
            return ResponseUtil.error("通知不存在");
        }
        
        System.out.println("markAsRead - 通知信息: receiverId=" + notification.getReceiverId() + ", isRead=" + notification.getIsRead());
        
        // 优先使用带条件的更新（如果 receiverId 匹配）
        int result = 0;
        if (receiverId.equals(notification.getReceiverId())) {
            result = notificationDao.markAsRead(id, receiverId);
        } else {
            // 如果 receiverId 不匹配，但记录存在，直接根据ID更新
            System.out.println("markAsRead - receiverId不匹配，使用markAsReadById");
            result = notificationDao.markAsReadById(id);
        }
        
        System.out.println("markAsRead - 更新结果: " + result);
        
        if (result > 0) {
            return ResponseUtil.success("标记成功");
        }
        // 如果还是失败，尝试无条件更新
        result = notificationDao.markAsReadById(id);
        if (result > 0) {
            return ResponseUtil.success("标记成功");
        }
        return ResponseUtil.error("标记失败");
    }
    
    @Override
    public ResponseObjectEntity markAllAsRead(String receiverId) {
        if (receiverId == null || receiverId.isEmpty()) {
            return ResponseUtil.error("用户ID不能为空");
        }
        notificationDao.markAllAsRead(receiverId);
        return ResponseUtil.success("标记成功");
    }
    
    @Override
    @Transactional
    public ResponseObjectEntity publishNotification(String title, String description, String type,
                                                   String publisherId, String receiverIds, String scope) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseUtil.error("通知标题不能为空");
        }
        
        String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date());
        int totalResult = 0;
        
        // 根据 scope 确定接收者列表
        List<String> receivers;
        if ("all".equals(scope) || receiverIds == null || receiverIds.trim().isEmpty()) {
            // 发布全体用户，使用原生 SQL 查询所有用户
            List<Map<String, Object>> allUsers = loginDao.selectAllUsersForNotification();
            receivers = new ArrayList<>();
            for (Map<String, Object> user : allUsers) {
                Object id = user.get("id");
                if (id != null) {
                    receivers.add(id.toString());
                }
            }
        } else {
            // 发布指定用户
            receivers = Arrays.asList(receiverIds.split(","));
        }
        
        if (receivers.isEmpty()) {
            return ResponseUtil.error("没有可发布的接收者");
        }
        
        // 生成批次ID
        String batchId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        
        // 创建通知数据用于 WebSocket 推送
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("title", title.trim());
        notificationData.put("description", description != null ? description.trim() : "");
        notificationData.put("type", type != null ? type : "info");
        notificationData.put("publisherId", publisherId);
        notificationData.put("createTime", now);
        notificationData.put("scope", scope);
        notificationData.put("totalReceivers", receivers.size());
        notificationData.put("batchId", batchId);
        
        for (String receiverId : receivers) {
            Notification notification = new Notification();
            notification.setTitle(title.trim());
            notification.setDescription(description != null ? description.trim() : "");
            notification.setType(type != null ? type : "info");
            notification.setIsRead(false);
            notification.setReceiverId(receiverId.trim());
            notification.setBatchId(batchId);
            notification.setPublisherId(publisherId);
            notification.setCreateTime(now);
            
            totalResult += notificationDao.insert(notification);
        }
        
        if (totalResult > 0) {
            // 通过 WebSocket 推送给在线的接收者
            pushNotificationToReceivers(receivers, notificationData);
            
            String msg = "all".equals(scope) ? "发布成功，已发送给全体用户（共" + totalResult + "人）" : "发布成功，已发送给" + totalResult + "位用户";
            return ResponseUtil.success(msg);
        }
        return ResponseUtil.error("发布失败");
    }
    
    /**
     * 通过 WebSocket 推送通知给在线接收者
     */
    private void pushNotificationToReceivers(List<String> receivers, Map<String, Object> notificationData) {
        try {
            // 过滤出在线用户
            List<String> onlineReceivers = new ArrayList<>();
            for (String receiverId : receivers) {
                if (com.example.spring_boot_mode.websocket.SessionManager.isUserOnline(receiverId)) {
                    onlineReceivers.add(receiverId);
                }
            }
            
            if (!onlineReceivers.isEmpty()) {
                // 推送给在线用户
                Map<String, Object> pushData = new HashMap<>();
                pushData.put("message", "您有一条新通知");
                pushData.put("notification", notificationData);
                pushData.put("totalUnread", getUnreadCountForReceivers(onlineReceivers));
                
                messageDispatcher.sendToUsers(onlineReceivers, "notification", pushData);
            }
        } catch (Exception e) {
            // WebSocket 推送失败不影响主流程
            System.err.println("WebSocket推送通知失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取接收者的未读通知数量（用于推送时附带统计）
     */
    private Map<String, Integer> getUnreadCountForReceivers(List<String> receivers) {
        Map<String, Integer> counts = new HashMap<>();
        for (String receiverId : receivers) {
            int count = notificationDao.countUnreadByReceiverId(receiverId);
            counts.put(receiverId, count);
        }
        return counts;
    }
    
    @Override
    public ResponseObjectEntity deleteNotification(Long id, String receiverId) {
        if (id == null) {
            return ResponseUtil.error("通知ID不能为空");
        }
        int result = notificationDao.deleteByIdAndReceiver(id, receiverId);
        if (result > 0) {
            return ResponseUtil.success("删除成功");
        }
        return ResponseUtil.error("删除失败");
    }
    
    @Override
    public ResponseObjectEntity getPublishedNotifications(String publisherId) {
        if (publisherId == null || publisherId.isEmpty()) {
            return ResponseUtil.error("发布者ID不能为空");
        }
        List<Notification> notifications = notificationDao.selectByPublisherId(publisherId);
        return ResponseUtil.success(notifications);
    }
    
    @Override
    @Transactional
    public ResponseObjectEntity deletePublishedNotification(Long id, String publisherId) {
        if (id == null) {
            return ResponseUtil.error("通知ID不能为空");
        }
        int result = notificationDao.deleteByIdAndPublisher(id, publisherId);
        if (result > 0) {
            return ResponseUtil.success("删除成功");
        }
        return ResponseUtil.error("删除失败");
    }
    
    @Override
    public ResponseObjectEntity getPublishedNotificationsPaged(String publisherId, int page, int size) {
        if (publisherId == null || publisherId.isEmpty()) {
            return ResponseUtil.error("发布者ID不能为空");
        }
        
        // 确保分页参数有效
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询分页数据
        List<Notification> notifications = notificationDao.selectByPublisherIdPaged(publisherId, offset, size);
        
        // 查询总数
        int total = notificationDao.countByPublisherId(publisherId);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", notifications);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", total == 0 ? 0 : (total - 1) / size + 1);
        
        return ResponseUtil.success(result);
    }

    @Override
    public ResponseObjectEntity getPublishedBatchesPaged(String publisherId, int page, int size) {
        if (publisherId == null || publisherId.isEmpty()) {
            return ResponseUtil.error("发布者ID不能为空");
        }
        
        // 确保分页参数有效
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询批次分页数据（已兼容batch_id为NULL的旧数据）
        List<Map<String, Object>> batches = notificationDao.selectPublishedBatchesPaged(publisherId, offset, size);
        
        // 转换字段名为驼峰格式
        List<Map<String, Object>> camelCaseBatches = convertToCamelCase(batches);
        
        // 查询批次总数
        long totalCount = notificationDao.countPublishedBatches(publisherId);
        int total = (int) totalCount;
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", camelCaseBatches);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", total == 0 ? 0 : (total - 1) / size + 1);
        
        return ResponseUtil.success(result);
    }

    /**
     * 将下划线命名的Map列表转换为驼峰命名
     */
    private List<Map<String, Object>> convertToCamelCase(List<Map<String, Object>> source) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : source) {
            Map<String, Object> camelItem = new HashMap<>();
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                String camelKey = toCamelCase(entry.getKey());
                Object value = entry.getValue();
                // 确保 isRead 字段值为 Integer 类型（处理 TINYINT -> Byte/Boolean/Number 的转换）
                if ("isRead".equals(camelKey)) {
                    if (value instanceof Boolean) {
                        value = (Boolean) value ? 1 : 0;
                    } else if (value instanceof Number) {
                        value = ((Number) value).intValue();
                    } else if (value != null) {
                        // 尝试字符串转换
                        value = Integer.parseInt(value.toString());
                    } else {
                        value = 0; // 默认未读
                    }
                }
                camelItem.put(camelKey, value);
            }
            result.add(camelItem);
        }
        return result;
    }

    /**
     * 下划线命名转驼峰命名
     */
    private String toCamelCase(String underscoreName) {
        if (underscoreName == null || !underscoreName.contains("_")) {
            return underscoreName;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < underscoreName.length(); i++) {
            char c = underscoreName.charAt(i);
            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    @Override
    public ResponseObjectEntity getBatchReceivers(String batchId) {
        if (batchId == null || batchId.isEmpty()) {
            return ResponseUtil.error("批次ID不能为空");
        }
        
        List<Map<String, Object>> receivers;
        
        // 判断是否为旧数据（以 single_ 开头的是单条旧数据）
        if (batchId.startsWith("single_")) {
            // 从 single_ID 中提取 ID
            String idStr = batchId.substring(7);
            try {
                Long id = Long.parseLong(idStr);
                receivers = notificationDao.selectByIdForBatch(id);
            } catch (NumberFormatException e) {
                return ResponseUtil.error("无效的批次ID");
            }
        } else {
            // 新数据按 batch_id 查询
            receivers = notificationDao.selectBatchReceivers(batchId);
        }
        
        // 转换字段名为驼峰格式
        List<Map<String, Object>> camelCaseReceivers = convertToCamelCase(receivers);
        return ResponseUtil.success(camelCaseReceivers);
    }

    @Override
    public ResponseObjectEntity getBatchReceiversPaged(String batchId, int page, int size) {
        if (batchId == null || batchId.isEmpty()) {
            return ResponseUtil.error("批次ID不能为空");
        }
        
        try {
            // 确保分页参数有效
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            if (size > 100) size = 100;
            
            // 计算偏移量
            int offset = (page - 1) * size;
            
            // 判断是否为旧数据（以 single_ 开头的是单条旧数据）
            List<Map<String, Object>> receivers;
            int total;
            int readCount = 0;
            
            if (batchId.startsWith("single_")) {
                // 从 single_ID 中提取 ID
                String idStr = batchId.substring(7);
                Long id = Long.parseLong(idStr);
                receivers = notificationDao.selectByIdForBatchPaged(id);
                total = receivers.size();
                // 统计已读数
                List<Map<String, Object>> allReceivers = notificationDao.selectByIdForBatch(id);
                readCount = (int) allReceivers.stream().filter(r -> {
                    Object val = r.get("is_read");
                    if (val == null) val = r.get("isRead");
                    return val != null && ((Number) val).intValue() == 1;
                }).count();
            } else {
                // 新数据按 batch_id 查询
                receivers = notificationDao.selectBatchReceiversPaged(batchId, offset, size);
                total = (int) notificationDao.countBatchReceivers(batchId);
                readCount = (int) notificationDao.countBatchReadReceivers(batchId);
            }
            
            // 转换字段名为驼峰格式
            List<Map<String, Object>> camelCaseReceivers = convertToCamelCase(receivers);
            
            // 日志输出，便于调试
            for (Map<String, Object> receiver : camelCaseReceivers) {
                System.out.println("接收者: " + receiver.get("receiverName") + ", isRead=" + receiver.get("isRead") + ", type=" + (receiver.get("isRead") != null ? receiver.get("isRead").getClass().getName() : "null"));
            }
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", camelCaseReceivers);
            result.put("total", total);
            result.put("readCount", readCount);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", total == 0 ? 0 : (total - 1) / size + 1);
            
            return ResponseUtil.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("查询接收者详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseObjectEntity deletePublishedBatch(String batchId, String publisherId) {
        if (batchId == null || batchId.isEmpty()) {
            return ResponseUtil.error("批次ID不能为空");
        }
        if (publisherId == null || publisherId.isEmpty()) {
            return ResponseUtil.error("发布者ID不能为空");
        }
        
        // 判断是否为旧数据（以 single_ 开头的是单条旧数据）
        if (batchId.startsWith("single_")) {
            // 从 single_ID 中提取 ID
            String idStr = batchId.substring(7);
            try {
                Long id = Long.parseLong(idStr);
                int result = notificationDao.deleteByIdAndPublisherSimple(id, publisherId);
                if (result > 0) {
                    return ResponseUtil.success("删除成功");
                }
                return ResponseUtil.error("删除失败");
            } catch (NumberFormatException e) {
                return ResponseUtil.error("无效的批次ID");
            }
        }
        
        // 新数据按 batch_id 删除
        int result = notificationDao.deleteBatchById(batchId, publisherId);
        if (result > 0) {
            return ResponseUtil.success("删除成功");
        }
        return ResponseUtil.error("删除失败");
    }

    @Override
    @Transactional
    public ResponseObjectEntity updatePublishedBatch(String batchId, String title, String description, String type, String publisherId) {
        if (batchId == null || batchId.isEmpty()) {
            return ResponseUtil.error("批次ID不能为空");
        }
        if (publisherId == null || publisherId.isEmpty()) {
            return ResponseUtil.error("发布者ID不能为空");
        }
        if (title == null || title.trim().isEmpty()) {
            return ResponseUtil.error("通知标题不能为空");
        }
        
        // 判断是否为旧数据（以 single_ 开头的是单条旧数据）
        if (batchId.startsWith("single_")) {
            // 从 single_ID 中提取 ID
            String idStr = batchId.substring(7);
            try {
                Long id = Long.parseLong(idStr);
                int result = notificationDao.updateSingleContent(id, title.trim(), description != null ? description.trim() : "", type != null ? type : "info", publisherId);
                if (result > 0) {
                    return ResponseUtil.success("更新成功");
                }
                return ResponseUtil.error("更新失败");
            } catch (NumberFormatException e) {
                return ResponseUtil.error("无效的批次ID");
            }
        }
        
        // 新数据按 batch_id 更新
        int result = notificationDao.updateBatchContent(batchId, title.trim(), description != null ? description.trim() : "", type != null ? type : "info", publisherId);
        if (result > 0) {
            return ResponseUtil.success("更新成功");
        }
        return ResponseUtil.error("更新失败");
    }
}