package com.example.spring_boot_mode.model.dao.messageCenter;

import com.example.spring_boot_mode.model.entity.messageCenter.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天消息数据访问接口
 */
@Mapper
public interface ChatMessageDao {

    /**
     * 新增聊天消息
     */
    int insert(ChatMessage chatMessage);

    /**
     * 查询最新 N 条消息（按 ID 倒序）
     */
    List<ChatMessage> selectLatestMessages(@Param("limit") int limit);

    /**
     * 分页查询聊天消息（基于 ID 向前翻页）
     *
     * @param lastId 当前已加载的最早消息ID；null 则取最新
     * @param limit  每页数量
     * @return 消息列表（按 id 倒序，需要调用方按业务需要排序）
     */
    List<ChatMessage> selectPageMessages(@Param("lastId") Long lastId, @Param("limit") int limit);

    /**
     * 查询最新 N 条消息（按 ID 升序返回，便于直接渲染）
     */
    List<ChatMessage> selectLatestMessagesAsc(@Param("limit") int limit);

    /**
     * 关键字搜索（按内容模糊匹配）
     *
     * @param keyword 关键字
     * @param limit   最大返回条数
     */
    List<ChatMessage> searchByContent(@Param("keyword") String keyword, @Param("limit") int limit);

    /**
     * 按发送者查询最近的消息
     */
    List<ChatMessage> selectBySender(@Param("senderId") String senderId, @Param("limit") int limit);

    /**
     * 查询消息总数
     */
    long selectTotalCount();

    /**
     * 根据ID查询消息
     */
    ChatMessage selectById(@Param("id") Long id);

    /**
     * 删除消息
     */
    int deleteById(@Param("id") Long id);

    /**
     * 物理清理：删除指定时间之前的消息（用于定时任务，防止表无限增长）
     *
     * @param cutoffTime yyyy-MM-dd HH:mm:ss 格式的截止时间字符串
     * @return 删除条数
     */
    int deleteBefore(@Param("cutoffTime") String cutoffTime);
}
