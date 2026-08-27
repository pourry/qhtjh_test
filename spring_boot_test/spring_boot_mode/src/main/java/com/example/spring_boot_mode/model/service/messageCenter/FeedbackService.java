package com.example.spring_boot_mode.model.service.messageCenter;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;

import java.util.Map;

/**
 * 反馈服务接口
 */
public interface FeedbackService {

    /**
     * 提交反馈
     */
    ResponseObjectEntity submitFeedback(String type, String title, String description,
                                        String contact, String images, String userId, String userName);

    /**
     * 获取我的反馈列表（完整，不分页）
     */
    ResponseObjectEntity getMyFeedbacks(String userId);

    /**
     * 分页获取我的反馈列表
     *
     * @param userId 用户ID
     * @param page   页码（从 1 开始）
     * @param size   每页条数
     */
    ResponseObjectEntity getMyFeedbacksPaged(String userId, int page, int size);

    /**
     * 获取我最近的 N 条反馈（用于 Top 浮层预览）
     */
    ResponseObjectEntity getRecentFeedbacks(String userId, int limit);

    /**
     * 获取反馈详情
     */
    ResponseObjectEntity getFeedbackDetail(Long id);

    /**
     * 删除反馈
     */
    ResponseObjectEntity deleteFeedback(Long id, String userId);

    /**
     * 更新反馈（用户编辑自己的反馈）
     */
    ResponseObjectEntity updateFeedback(Long id, String type, String title,
                                        String description, String contact, String images,
                                        String userId);

    /**
     * 获取所有反馈列表（管理员，不分页）
     */
    ResponseObjectEntity getAllFeedbacks(Map<String, Object> params);

    /**
     * 分页获取所有反馈列表（管理员）
     */
    ResponseObjectEntity getAllFeedbacksPaged(Map<String, Object> params, int page, int size);

    /**
     * 处理反馈（更新状态）
     */
    ResponseObjectEntity processFeedback(Long id, String status, String handlerId);

    /**
     * 回复反馈
     */
    ResponseObjectEntity replyFeedback(Long id, String reply, String handlerId);

    /**
     * 获取反馈统计数据
     */
    ResponseObjectEntity getFeedbackStats();
}
