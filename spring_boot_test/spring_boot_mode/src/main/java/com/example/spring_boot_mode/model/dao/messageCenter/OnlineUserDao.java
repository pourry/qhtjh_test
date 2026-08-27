package com.example.spring_boot_mode.model.dao.messageCenter;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 在线用户数据访问接口
 */
@Mapper
public interface OnlineUserDao {
    
    /**
     * 查询所有在线用户
     */
    List<Map<String, Object>> selectOnlineUsers();
    
    /**
     * 查询指定用户的在线状态
     */
    Map<String, Object> selectOnlineUserById(@Param("userId") String userId);
}