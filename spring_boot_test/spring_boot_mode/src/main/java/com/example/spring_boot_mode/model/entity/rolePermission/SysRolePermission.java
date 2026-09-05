package com.example.spring_boot_mode.model.entity.rolePermission;

import lombok.Data;

/**
 * 角色-权限关联实体类
 * 对应数据库 sys_role_permission 表
 */
@Data
public class SysRolePermission {
    /** 主键ID */
    private String id;
    /** 角色ID */
    private String roleId;
    /** 权限ID */
    private String permissionId;
    /** 创建时间 */
    private String createTime;
}
