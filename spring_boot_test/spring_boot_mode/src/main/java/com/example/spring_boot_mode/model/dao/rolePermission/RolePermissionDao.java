package com.example.spring_boot_mode.model.dao.rolePermission;

import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.model.entity.rolePermission.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色-权限关联 DAO
 */
@Mapper
public interface RolePermissionDao {

    /** 根据角色ID查询关联的权限ID列表 */
    List<String> selectPermissionIdsByRoleId(@Param("roleId") String roleId);

    /** 根据权限ID查询关联的角色ID列表 */
    List<String> selectRoleIdsByPermissionId(@Param("permissionId") String permissionId);

    /** 批量插入角色-权限关联 */
    int batchInsert(@Param("list") List<SysRolePermission> list);

    /** 根据角色ID删除所有关联 */
    int deleteByRoleId(@Param("roleId") String roleId);

    /** 根据权限ID删除所有关联 */
    int deleteByPermissionId(@Param("permissionId") String permissionId);

    /** 更新用户的角色ID */
    int updateUserRoleId(@Param("userId") String userId, @Param("roleId") String roleId);

    /** 分页查询用户（带关键字搜索），返回原生 SQL 结果 */
    List<SysUser> selectUserPage(@Param("offset") int offset,
                                 @Param("size") int size,
                                 @Param("keyword") String keyword);

    /** 统计用户总数（带关键字搜索） */
    long countUsers(@Param("keyword") String keyword);
}
