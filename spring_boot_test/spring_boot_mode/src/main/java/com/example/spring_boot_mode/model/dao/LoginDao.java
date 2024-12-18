package com.example.spring_boot_mode.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring_boot_mode.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginDao extends BaseMapper<SysUser> {

    SysUser tologin(String username);

    int tosignUp(SysUser sysUser);

    int tocheckname(String username);
}
