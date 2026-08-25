package com.example.spring_boot_mode.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring_boot_mode.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginDao extends BaseMapper<SysUser> {

    SysUser tologin(String username);

    int tosignUp(SysUser sysUser);

    int tocheckname(String username);

    /** 根据ID查询用户 */
    SysUser selectById(String id);

    /** 更新用户基本信息 */
    int updateInfo(SysUser sysUser);

    /** 更新密码 */
    int updatePassword(@Param("id") String id,
                       @Param("oldPassword") String oldPassword,
                       @Param("newPassword") String newPassword,
                       @Param("updateTime") String updateTime);
}
