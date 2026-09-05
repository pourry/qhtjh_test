package com.example.spring_boot_mode.model.dao.reminder;

import com.example.spring_boot_mode.model.entity.reminder.Reminder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒数据访问接口
 */
@Mapper
public interface ReminderDao {

    /**
     * 插入提醒
     */
    int insert(Reminder reminder);

    /**
     * 根据ID更新提醒
     */
    int updateById(Reminder reminder);

    /**
     * 根据ID删除提醒
     */
    int deleteById(@Param("id") String id);

    /**
     * 根据ID查询提醒
     */
    Reminder selectById(@Param("id") String id);

    /**
     * 分页查询我的提醒列表
     */
    List<Reminder> selectMyList(@Param("userId") String userId,
                                @Param("status") String status,
                                @Param("targetType") String targetType,
                                @Param("keyword") String keyword,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    /**
     * 统计我的提醒总数
     */
    int selectMyListTotal(@Param("userId") String userId,
                          @Param("status") String status,
                          @Param("targetType") String targetType,
                          @Param("keyword") String keyword);

    /**
     * 查询待触发且已到提醒时间的提醒
     */
    List<Reminder> selectPendingReminders(@Param("beforeTime") LocalDateTime beforeTime);

    /**
     * 根据目标信息查询提醒
     */
    Reminder selectByTarget(@Param("targetType") String targetType,
                            @Param("targetId") String targetId,
                            @Param("userId") String userId);

    /**
     * 根据条件查询单条记录
     */
    Reminder selectOneByCondition(@Param("targetType") String targetType,
                                   @Param("targetId") String targetId,
                                   @Param("userId") String userId,
                                   @Param("isOpen") Integer isOpen,
                                   @Param("status") String status);

    /**
     * 统计某用户已触发但未读的提醒数量
     */
    int countTriggeredUnread(@Param("userId") String userId);

    /**
     * 标记单条提醒为已读
     */
    int markAsRead(@Param("id") String id);

    /**
     * 标记某用户所有已触发提醒为已读
     */
    int markAllAsRead(@Param("userId") String userId);

    /** 查询所有提醒（调试用） */
    List<Reminder> selectAll();
}
