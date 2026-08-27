package com.example.spring_boot_mode.model.dao.messageCenter;

import com.example.spring_boot_mode.model.entity.messageCenter.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通知数据访问接口
 */
@Mapper
public interface NotificationDao {
    
    /**
     * 新增通知
     */
    int insert(Notification notification);
    
    /**
     * 根据ID查询通知
     */
    Notification selectById(@Param("id") Long id);
    
    /**
     * 查询用户的通知列表
     */
    List<Notification> selectByReceiverId(@Param("receiverId") String receiverId);
    
    /**
     * 分页查询用户的通知列表
     */
    List<Notification> selectByReceiverIdPaged(@Param("receiverId") String receiverId,
                                                @Param("offset") int offset,
                                                @Param("size") int size);
    
    /**
     * 统计用户通知总数
     */
    int countByReceiverId(@Param("receiverId") String receiverId);
    
    /**
     * 查询用户未读通知数量
     */
    int countUnreadByReceiverId(@Param("receiverId") String receiverId);
    
    /**
     * 标记单条通知为已读
     */
    int markAsRead(@Param("id") Long id, @Param("receiverId") String receiverId);
    
    /**
     * 根据ID标记为已读（不验证接收者）
     */
    int markAsReadById(@Param("id") Long id);
    
    /**
     * 标记所有通知为已读
     */
    int markAllAsRead(@Param("receiverId") String receiverId);
    
    /**
     * 删除通知
     */
    int deleteByIdAndReceiver(@Param("id") Long id, @Param("receiverId") String receiverId);
    
    /**
     * 查询发布者发布的通知列表
     */
    List<Notification> selectByPublisherId(@Param("publisherId") String publisherId);
    
    /**
     * 根据ID删除通知（发布者删除自己发布的通知）
     */
    int deleteByIdAndPublisher(@Param("id") Long id, @Param("publisherId") String publisherId);

    /**
     * 分页查询发布者发布的通知列表
     */
    List<Notification> selectByPublisherIdPaged(@Param("publisherId") String publisherId, 
                                                @Param("offset") int offset, 
                                                @Param("size") int size);
    
    /**
     * 统计发布者发布的通知总数
     */
    int countByPublisherId(@Param("publisherId") String publisherId);

    /**
     * 按批次分页查询发布者发布的通知列表
     * 兼容batch_id为NULL的旧数据
     */
    List<Map<String, Object>> selectPublishedBatchesPaged(@Param("publisherId") String publisherId,
                                                          @Param("offset") int offset,
                                                          @Param("size") int size);

    /**
     * 统计发布者发布的批次总数（兼容旧数据）
     */
    long countPublishedBatches(@Param("publisherId") String publisherId);

    /**
     * 根据批次ID查询接收者列表
     */
    List<Map<String, Object>> selectBatchReceivers(@Param("batchId") String batchId);

    /**
     * 根据批次ID分页查询接收者列表
     */
    List<Map<String, Object>> selectBatchReceiversPaged(@Param("batchId") String batchId,
                                                        @Param("offset") int offset,
                                                        @Param("size") int size);

    /**
     * 统计批次接收者总数
     */
    long countBatchReceivers(@Param("batchId") String batchId);

    /**
     * 统计批次已读通知数
     */
    long countBatchReadReceivers(@Param("batchId") String batchId);

    /**
     * 根据ID查询单条通知详情（用于batch_id为NULL的旧数据）
     */
    List<Map<String, Object>> selectByIdForBatch(@Param("id") Long id);

    /**
     * 根据ID分页查询单条通知（用于batch_id为NULL的旧数据，兼容分页）
     */
    List<Map<String, Object>> selectByIdForBatchPaged(@Param("id") Long id);

    /**
     * 根据批次ID删除所有通知
     */
    int deleteBatchById(@Param("batchId") String batchId, @Param("publisherId") String publisherId);

    /**
     * 根据ID删除单条通知
     */
    int deleteByIdAndPublisherSimple(@Param("id") Long id, @Param("publisherId") String publisherId);

    /**
     * 根据批次ID更新通知内容
     */
    int updateBatchContent(@Param("batchId") String batchId, 
                           @Param("title") String title, 
                           @Param("description") String description,
                           @Param("type") String type,
                           @Param("publisherId") String publisherId);

    /**
     * 根据ID更新单条通知内容（用于旧数据）
     */
    int updateSingleContent(@Param("id") Long id,
                            @Param("title") String title,
                            @Param("description") String description,
                            @Param("type") String type,
                            @Param("publisherId") String publisherId);
}