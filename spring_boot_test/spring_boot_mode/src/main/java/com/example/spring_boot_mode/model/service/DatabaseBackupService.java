package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.model.entity.DatabaseBackup;

import java.util.List;

/**
 * 数据库备份服务接口
 */
public interface DatabaseBackupService {
    
    /**
     * 备份数据库
     * @return 备份文件路径
     */
    String backupDatabase() throws Exception;
    
    /**
     * 查询备份记录列表
     */
    List<DatabaseBackup> getBackupList();
}
