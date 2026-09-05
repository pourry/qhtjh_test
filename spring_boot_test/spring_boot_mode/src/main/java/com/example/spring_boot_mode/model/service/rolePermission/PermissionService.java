package com.example.spring_boot_mode.model.service.rolePermission;

import com.example.spring_boot_mode.model.entity.rolePermission.SysPermission;

import java.util.List;

/**
 * 权限 Service 接口
 */
public interface PermissionService {

    /** 查询所有权限 */
    List<SysPermission> listAll();

    /** 根据ID查询权限 */
    SysPermission getById(String id);

    /** 根据权限编码查询权限 */
    SysPermission getByCode(String permissionCode);

    /** 根据用户ID查询权限列表 */
    List<SysPermission> listByUserId(String userId);

    /** 新增权限 */
    SysPermission create(SysPermission permission);

    /** 更新权限（内置权限不能修改 permissionCode 和 isBuiltin） */
    SysPermission update(SysPermission permission);

    /** 删除权限（内置权限不能删除） */
    boolean delete(String id);
}
