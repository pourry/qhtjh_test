package com.example.spring_boot_mode.model.entity.rolePermission;

import lombok.Data;

/**
 * 权限/页面实体类
 * 对应数据库 sys_permission 表
 */
@Data
public class SysPermission {
    /** 主键ID */
    private String id;
    /** 权限名称（显示用） */
    private String permissionName;
    /** 权限编码（唯一标识，如 page:user:list） */
    private String permissionCode;
    /** 对应路由路径（如 /favorites, /userSelf/carousel） */
    private String path;
    /** 所属分组（如 收藏夹、我的、消息中心） */
    private String groupName;
    /** 描述 */
    private String description;
    /** 是否内置权限（内置权限不可删除）：0-否, 1-是 */
    private Integer isBuiltin;
    /** 排序序号 */
    private Integer sort;
    /** 创建时间 */
    private String createTime;
    /** 更新时间 */
    private String updateTime;
}
