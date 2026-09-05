package com.example.spring_boot_mode.model.service.rolePermission.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.spring_boot_mode.exception.ThrowMsgException;
import com.example.spring_boot_mode.model.dao.rolePermission.PermissionDao;
import com.example.spring_boot_mode.model.dao.rolePermission.RoleDao;
import com.example.spring_boot_mode.model.dao.rolePermission.RolePermissionDao;
import com.example.spring_boot_mode.model.entity.rolePermission.SysPermission;
import com.example.spring_boot_mode.model.entity.rolePermission.SysRole;
import com.example.spring_boot_mode.model.entity.rolePermission.SysRolePermission;
import com.example.spring_boot_mode.model.service.rolePermission.RoleService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色 Service 实现
 */
@DS("mode")
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public List<SysRole> listAll() {
        return roleDao.selectAllRoles();
    }

    @Override
    public SysRole getById(String id) {
        return roleDao.selectById(id);
    }

    @Override
    public SysRole getByCode(String roleCode) {
        return roleDao.selectByCode(roleCode);
    }

    @Override
    public SysRole create(SysRole role) {
        // 校验
        if (StringUtils.isEmpty(role.getRoleName())) {
            throw new ThrowMsgException("角色名称不能为空");
        }
        if (StringUtils.isEmpty(role.getRoleCode())) {
            throw new ThrowMsgException("角色编码不能为空");
        }
        // 编码不能重复
        SysRole exist = roleDao.selectByCode(role.getRoleCode());
        if (exist != null) {
            throw new ThrowMsgException("角色编码已存在：" + role.getRoleCode());
        }

        role.setId(UUidUtil.getuuid());
        role.setIsBuiltin(0);
        role.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
        role.setUpdateTime(role.getCreateTime());
        roleDao.insert(role);
        return role;
    }

    @Override
    public SysRole update(SysRole role) {
        if (StringUtils.isEmpty(role.getId())) {
            throw new ThrowMsgException("角色ID不能为空");
        }
        SysRole exist = roleDao.selectById(role.getId());
        if (exist == null) {
            throw new ThrowMsgException("角色不存在");
        }
        // 内置角色禁止修改 roleCode 和 isBuiltin
        if (exist.getIsBuiltin() != null && exist.getIsBuiltin() == 1) {
            role.setRoleCode(null);
            role.setIsBuiltin(null);
        }
        role.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
        roleDao.update(role);
        return roleDao.selectById(role.getId());
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        SysRole exist = roleDao.selectById(id);
        if (exist == null) {
            throw new ThrowMsgException("角色不存在");
        }
        // 内置角色禁止删除
        if (exist.getIsBuiltin() != null && exist.getIsBuiltin() == 1) {
            throw new ThrowMsgException("内置角色禁止删除");
        }
        // 先删除关联
        rolePermissionDao.deleteByRoleId(id);
        roleDao.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public void assignPermissions(String roleId, List<String> permissionIds) {
        SysRole role = roleDao.selectById(roleId);
        if (role == null) {
            throw new ThrowMsgException("角色不存在");
        }
        // 清除旧关联
        rolePermissionDao.deleteByRoleId(roleId);
        // 插入新关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date());
            List<SysRolePermission> list = new ArrayList<>();
            for (String pid : permissionIds) {
                SysRolePermission rp = new SysRolePermission();
                rp.setId(UUidUtil.getuuid());
                rp.setRoleId(roleId);
                rp.setPermissionId(pid);
                rp.setCreateTime(now);
                list.add(rp);
            }
            rolePermissionDao.batchInsert(list);
        }
    }

    @Override
    public Map<String, Object> getRoleWithPermissions(String roleId) {
        SysRole role = roleDao.selectById(roleId);
        if (role == null) {
            throw new ThrowMsgException("角色不存在");
        }
        List<SysPermission> permissions = permissionDao.selectByRoleId(roleId);
        List<String> permissionIds = permissions.stream().map(SysPermission::getId).collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", role.getId());
        result.put("roleName", role.getRoleName());
        result.put("roleCode", role.getRoleCode());
        result.put("description", role.getDescription());
        result.put("isBuiltin", role.getIsBuiltin());
        result.put("createTime", role.getCreateTime());
        result.put("updateTime", role.getUpdateTime());
        result.put("permissionIds", permissionIds);
        result.put("permissions", permissions);
        return result;
    }

    @Override
    public List<Map<String, Object>> listAllWithPermissions() {
        List<SysRole> roles = roleDao.selectAllRoles();
        List<Map<String, Object>> result = new ArrayList<>();
        for (SysRole role : roles) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", role.getId());
            item.put("roleName", role.getRoleName());
            item.put("roleCode", role.getRoleCode());
            item.put("description", role.getDescription());
            item.put("isBuiltin", role.getIsBuiltin());
            item.put("createTime", role.getCreateTime());
            item.put("updateTime", role.getUpdateTime());
            List<String> pids = rolePermissionDao.selectPermissionIdsByRoleId(role.getId());
            item.put("permissionIds", pids);
            result.add(item);
        }
        return result;
    }
}
