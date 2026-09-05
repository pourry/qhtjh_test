package com.example.spring_boot_mode.model.entity.rolePermission;

import lombok.Data;

/**
 * 角色实体类
 * 对应数据库 sys_role 表
 */
@Data
public class SysRole {
    /** 主键ID */
    private String id;
    /** 角色名称（显示用） */
    private String roleName;
    /** 角色编码（唯一标识，如 admin/user） */
    private String roleCode;
    /** 角色描述 */
    private String description;
    /** 是否内置角色（内置角色不可删除）：0-否, 1-是 */
    private Integer isBuiltin;
    /** 创建时间 */
    private String createTime;
    /** 更新时间 */
    private String updateTime;
}
