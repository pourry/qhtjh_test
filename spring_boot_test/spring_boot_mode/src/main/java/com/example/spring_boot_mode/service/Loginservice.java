package com.example.spring_boot_mode.service;

import com.example.spring_boot_mode.entity.flowable.Flowable;
import com.example.spring_boot_mode.entity.mode.SysUser;

import java.util.List;

public interface Loginservice {
    List<SysUser> getSysUser();

    List<Flowable> getflowable();
}
