package com.example.spring_boot_mode.model.dao.rolePermission;

import com.example.spring_boot_mode.model.entity.rolePermission.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 DAO
 */
@Mapper
public interface RoleDao {

    /** 分页查询角色（原生 SQL，带关键字搜索） */
    List<SysRole> selectRolePage(@Param("offset") int offset,
                                  @Param("size") int size,
                                  @Param("keyword") String keyword);

    /** 统计角色总数（带关键字搜索） */
    long countRoles(@Param("keyword") String keyword);

    /** 查询所有角色 */
    List<SysRole> selectAllRoles();

    /** 根据ID查询角色 */
    SysRole selectById(@Param("id") String id);

    /** 根据角色编码查询角色 */
    SysRole selectByCode(@Param("roleCode") String roleCode);

    /** 新增角色 */
    int insert(SysRole role);

    /** 更新角色 */
    int update(SysRole role);

    /** 删除角色 */
    int deleteById(@Param("id") String id);

    /** 根据ID列表批量查询角色 */
    List<SysRole> selectByIds(@Param("ids") List<String> ids);
}
