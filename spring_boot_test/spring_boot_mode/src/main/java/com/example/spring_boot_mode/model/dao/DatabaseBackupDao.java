package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.DatabaseBackup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据库备份 DAO
 */
@Mapper
public interface DatabaseBackupDao {
    
    /**
     * 获取所有表名
     */
    List<String> getAllTables(@Param("databaseName") String databaseName);
    
    /**
     * 获取表的创建语句
     */
    String getTableCreateSql(@Param("tableName") String tableName);
    
    /**
     * 获取表的所有数据
     */
    List<String> getTableData(@Param("tableName") String tableName);
    
    /**
     * 保存备份记录
     */
    int insertBackupRecord(DatabaseBackup backup);
    
    /**
     * 查询备份记录列表
     */
    List<DatabaseBackup> selectBackupList();
}
