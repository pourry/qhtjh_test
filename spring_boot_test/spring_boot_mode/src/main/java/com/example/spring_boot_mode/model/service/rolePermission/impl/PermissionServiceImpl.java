package com.example.spring_boot_mode.model.service.rolePermission.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.spring_boot_mode.exception.ThrowMsgException;
import com.example.spring_boot_mode.model.dao.rolePermission.PermissionDao;
import com.example.spring_boot_mode.model.dao.rolePermission.RolePermissionDao;
import com.example.spring_boot_mode.model.entity.rolePermission.SysPermission;
import com.example.spring_boot_mode.model.service.rolePermission.PermissionService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 权限 Service 实现
 */
@DS("mode")
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public List<SysPermission> listAll() {
        return permissionDao.selectAllPermissions();
    }

    @Override
    public SysPermission getById(String id) {
        return permissionDao.selectById(id);
    }

    @Override
    public SysPermission getByCode(String permissionCode) {
        return permissionDao.selectByCode(permissionCode);
    }

    @Override
    public List<SysPermission> listByUserId(String userId) {
        return permissionDao.selectByUserId(userId);
    }

    @Override
    public SysPermission create(SysPermission permission) {
        if (StringUtils.isEmpty(permission.getPermissionName())) {
            throw new ThrowMsgException("权限名称不能为空");
        }
        if (StringUtils.isEmpty(permission.getPermissionCode())) {
            throw new ThrowMsgException("权限编码不能为空");
        }
        // 编码不能重复
        SysPermission exist = permissionDao.selectByCode(permission.getPermissionCode());
        if (exist != null) {
            throw new ThrowMsgException("权限编码已存在：" + permission.getPermissionCode());
        }

        permission.setId(UUidUtil.getuuid());
        if (permission.getSort() == null) {
            permission.setSort(0);
        }
        permission.setIsBuiltin(0);
        String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date());
        permission.setCreateTime(now);
        permission.setUpdateTime(now);
        permissionDao.insert(permission);
        return permission;
    }

    @Override
    public SysPermission update(SysPermission permission) {
        if (StringUtils.isEmpty(permission.getId())) {
            throw new ThrowMsgException("权限ID不能为空");
        }
        SysPermission exist = permissionDao.selectById(permission.getId());
        if (exist == null) {
            throw new ThrowMsgException("权限不存在");
        }
        // 内置权限禁止修改 permissionCode 和 isBuiltin
        if (exist.getIsBuiltin() != null && exist.getIsBuiltin() == 1) {
            permission.setPermissionCode(null);
            permission.setIsBuiltin(null);
        }
        permission.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
        permissionDao.update(permission);
        return permissionDao.selectById(permission.getId());
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        SysPermission exist = permissionDao.selectById(id);
        if (exist == null) {
            throw new ThrowMsgException("权限不存在");
        }
        // 内置权限禁止删除
        if (exist.getIsBuiltin() != null && exist.getIsBuiltin() == 1) {
            throw new ThrowMsgException("内置权限禁止删除");
        }
        // 先删除关联
        rolePermissionDao.deleteByPermissionId(id);
        permissionDao.deleteById(id);
        return true;
    }
}
