package com.example.spring_boot_mode.model.service.messageCenter;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;

/**
 * 通知服务接口
 */
public interface NotificationService {
    
    /**
     * 获取用户通知列表
     * @param receiverId 接收者ID
     * @return 通知列表
     */
    ResponseObjectEntity getNotifications(String receiverId);
    
    /**
     * 获取未读通知数量
     * @param receiverId 接收者ID
     * @return 未读数量
     */
    ResponseObjectEntity getUnreadCount(String receiverId);
    
    /**
     * 标记通知为已读
     * @param id 通知ID
     * @param receiverId 接收者ID
     * @return 操作结果
     */
    ResponseObjectEntity markAsRead(Long id, String receiverId);
    
    /**
     * 标记所有通知为已读
     * @param receiverId 接收者ID
     * @return 操作结果
     */
    ResponseObjectEntity markAllAsRead(String receiverId);
    
    /**
     * 发布通知（管理员功能）
     * @param title 通知标题
     * @param description 通知内容
     * @param type 通知类型
     * @param publisherId 发布者ID
     * @param receiverIds 接收者ID列表（指定用户时使用）
     * @param scope 发布范围（all-全体用户，specific-指定用户）
     * @return 发布结果
     */
    ResponseObjectEntity publishNotification(String title, String description, String type, 
                                            String publisherId, String receiverIds, String scope);
    
    /**
     * 删除通知
     * @param id 通知ID
     * @param receiverId 接收者ID
     * @return 操作结果
     */
    ResponseObjectEntity deleteNotification(Long id, String receiverId);
    
    /**
     * 获取发布者发布的通知列表
     * @param publisherId 发布者ID
     * @return 通知列表
     */
    ResponseObjectEntity getPublishedNotifications(String publisherId);
    
    /**
     * 删除发布者发布的通知
     * @param id 通知ID
     * @param publisherId 发布者ID
     * @return 操作结果
     */
    ResponseObjectEntity deletePublishedNotification(Long id, String publisherId);
    
    /**
     * 获取发布者发布的通知列表（支持分页）
     * @param publisherId 发布者ID
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页结果
     */
    ResponseObjectEntity getPublishedNotificationsPaged(String publisherId, int page, int size);

    /**
     * 获取发布者发布的批次通知列表（支持分页，按批次聚合）
     * @param publisherId 发布者ID
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页结果（按批次）
     */
    ResponseObjectEntity getPublishedBatchesPaged(String publisherId, int page, int size);

    /**
     * 获取批次的接收者详情
     * @param batchId 批次ID
     * @return 接收者列表
     */
    ResponseObjectEntity getBatchReceivers(String batchId);

    /**
     * 获取批次的接收者详情（支持分页）
     * @param batchId 批次ID
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页结果
     */
    ResponseObjectEntity getBatchReceiversPaged(String batchId, int page, int size);

    /**
     * 删除发布者发布的批次
     * @param batchId 批次ID
     * @param publisherId 发布者ID
     * @return 操作结果
     */
    ResponseObjectEntity deletePublishedBatch(String batchId, String publisherId);

    /**
     * 更新发布者发布的批次内容
     * @param batchId 批次ID
     * @param title 新标题
     * @param description 新内容
     * @param type 新类型
     * @param publisherId 发布者ID
     * @return 操作结果
     */
    ResponseObjectEntity updatePublishedBatch(String batchId, String title, String description, String type, String publisherId);
}