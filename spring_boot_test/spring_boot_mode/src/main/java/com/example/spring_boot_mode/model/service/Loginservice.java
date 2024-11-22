package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.SysUser;

import java.util.List;
import java.util.Map;

public interface Loginservice {
    List<SysUser> getSysUser();

    Map<String, Object> tologin(SysUser sysUser);

    ResponseObjectEntity tosignUp(SysUser sysUser);

    ResponseObjectEntity tocheckname(String username);
}
