package com.example.spring_boot_mode.model.dao.messageCenter;

import com.example.spring_boot_mode.model.entity.messageCenter.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 反馈数据访问接口
 */
@Mapper
public interface FeedbackDao {

    /**
     * 新增反馈
     */
    int insert(Feedback feedback);

    /**
     * 根据ID查询反馈
     */
    Feedback selectById(@Param("id") Long id);

    /**
     * 查询用户的反馈列表（完整，不分页）
     */
    List<Feedback> selectByUserId(@Param("userId") String userId);

    /**
     * 分页查询用户的反馈列表
     */
    List<Feedback> selectByUserIdPaged(@Param("userId") String userId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    /**
     * 统计用户的反馈条数
     */
    long countByUserId(@Param("userId") String userId);

    /**
     * 查询用户最近 N 条反馈（用于 Top 浮层预览）
     */
    List<Feedback> selectRecentByUserId(@Param("userId") String userId,
                                        @Param("limit") int limit);

    /**
     * 查询所有反馈列表（管理员）
     */
    List<Feedback> selectAllWithFilter(@Param("params") Map<String, Object> params);

    /**
     * 分页查询所有反馈（管理员）
     */
    List<Feedback> selectAllWithFilterPaged(@Param("params") Map<String, Object> params,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    /**
     * 统计筛选后的反馈条数
     */
    long countAllWithFilter(@Param("params") Map<String, Object> params);

    /**
     * 根据ID查询反馈详情
     */
    Feedback selectDetailById(@Param("id") Long id);

    /**
     * 更新反馈状态
     */
    int updateStatus(@Param("id") Long id,
                     @Param("status") String status,
                     @Param("handlerId") String handlerId,
                     @Param("updateTime") String updateTime);

    /**
     * 添加管理员回复
     */
    int addReply(@Param("id") Long id,
                 @Param("reply") String reply,
                 @Param("handlerId") String handlerId,
                 @Param("updateTime") String updateTime);

    /**
     * 删除反馈
     */
    int deleteByIdAndUser(@Param("id") Long id, @Param("userId") String userId);

    /**
     * 根据ID和用户ID查询反馈
     */
    Feedback selectByIdAndUser(@Param("id") Long id, @Param("userId") String userId);

    /**
     * 更新反馈内容
     */
    int updateFeedback(@Param("id") Long id,
                       @Param("type") String type,
                       @Param("title") String title,
                       @Param("description") String description,
                       @Param("contact") String contact,
                       @Param("images") String images,
                       @Param("updateTime") String updateTime);

    /**
     * 统计反馈数据
     */
    Map<String, Object> countStats();
}
