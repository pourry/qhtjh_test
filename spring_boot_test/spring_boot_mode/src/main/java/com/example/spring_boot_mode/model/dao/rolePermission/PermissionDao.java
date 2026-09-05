package com.example.spring_boot_mode.model.dao.rolePermission;

import com.example.spring_boot_mode.model.entity.rolePermission.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限/页面 DAO
 */
@Mapper
public interface PermissionDao {

    /** 分页查询权限（原生 SQL，带关键字搜索） */
    List<SysPermission> selectPermissionPage(@Param("offset") int offset,
                                            @Param("size") int size,
                                            @Param("keyword") String keyword);

    /** 统计权限总数（带关键字搜索） */
    long countPermissions(@Param("keyword") String keyword);

    /** 查询所有权限 */
    List<SysPermission> selectAllPermissions();

    /** 根据ID查询权限 */
    SysPermission selectById(@Param("id") String id);

    /** 根据权限编码查询权限 */
    SysPermission selectByCode(@Param("permissionCode") String permissionCode);

    /** 根据角色ID查询该角色拥有的权限列表 */
    List<SysPermission> selectByRoleId(@Param("roleId") String roleId);

    /** 根据角色编码查询该角色拥有的权限列表 */
    List<SysPermission> selectByRoleCode(@Param("roleCode") String roleCode);

    /** 根据用户ID查询该用户拥有的权限列表 */
    List<SysPermission> selectByUserId(@Param("userId") String userId);

    /** 新增权限 */
    int insert(SysPermission permission);

    /** 更新权限 */
    int update(SysPermission permission);

    /** 删除权限 */
    int deleteById(@Param("id") String id);
}
