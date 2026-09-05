package com.example.spring_boot_mode.model.service.rolePermission;

import com.example.spring_boot_mode.model.entity.rolePermission.SysRole;

import java.util.List;
import java.util.Map;

/**
 * 角色 Service 接口
 */
public interface RoleService {

    /** 查询所有角色 */
    List<SysRole> listAll();

    /** 根据ID查询角色 */
    SysRole getById(String id);

    /** 根据角色编码查询角色 */
    SysRole getByCode(String roleCode);

    /** 新增角色 */
    SysRole create(SysRole role);

    /** 更新角色（内置角色不能修改 roleCode 和 isBuiltin） */
    SysRole update(SysRole role);

    /** 删除角色（内置角色不能删除） */
    boolean delete(String id);

    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissions(String roleId, List<String> permissionIds);

    /** 查询角色及其权限列表（用于前端展示） */
    Map<String, Object> getRoleWithPermissions(String roleId);

    /** 查询所有角色及每个角色的权限ID列表（用于前端勾选） */
    List<Map<String, Object>> listAllWithPermissions();
}
