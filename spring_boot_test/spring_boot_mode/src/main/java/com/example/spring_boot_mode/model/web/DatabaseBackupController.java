package com.example.spring_boot_mode.model.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.DatabaseBackup;
import com.example.spring_boot_mode.model.service.DatabaseBackupService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据库备份控制器
 */
@RestController
@RequestMapping("/api/database/backup")
@Api(tags = "数据库备份管理")
public class DatabaseBackupController {

    private static final Logger log = LoggerFactory.getLogger(DatabaseBackupController.class);

    @Autowired
    private DatabaseBackupService databaseBackupService;

    /**
     * 执行数据库备份
     */
    @PostMapping("/execute")
    @ApiOperation("执行数据库备份")
    public ResponseObjectEntity backupDatabase() {
        try {
            log.info("收到数据库备份请求");
            String filePath = databaseBackupService.backupDatabase();
            log.info("数据库备份成功，文件路径: {}", filePath);
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("message", "数据库备份成功");
            result.put("filePath", filePath);
            
            return ResponseUtil.success(result);
        } catch (Exception e) {
            log.error("数据库备份失败", e);
            return ResponseUtil.error("数据库备份失败: " + e.getMessage());
        }
    }

    /**
     * 查询备份记录列表
     */
    @GetMapping("/list")
    @ApiOperation("查询备份记录列表")
    public ResponseObjectEntity getBackupList() {
        try {
            List<DatabaseBackup> list = databaseBackupService.getBackupList();
            return ResponseUtil.success(list);
        } catch (Exception e) {
            log.error("查询备份记录失败", e);
            return ResponseUtil.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取备份文件列表（从文件系统读取）
     */
    @GetMapping("/files")
    @ApiOperation("获取备份文件列表")
    public ResponseObjectEntity getBackupFiles() {
        try {
            String projectPath = System.getProperty("user.dir");
            String backupDir = projectPath + "/src/main/resources/sql";
            
            java.io.File dir = new java.io.File(backupDir);
            if (!dir.exists() || !dir.isDirectory()) {
                return ResponseUtil.success(new java.util.ArrayList<>());
            }

            java.io.File[] files = dir.listFiles((d, name) -> name.startsWith("primary_db_") && name.endsWith(".sql"));
            
            java.util.List<java.util.Map<String, Object>> fileList = new java.util.ArrayList<>();
            if (files != null) {
                for (java.io.File file : files) {
                    java.util.Map<String, Object> fileInfo = new java.util.HashMap<>();
                    fileInfo.put("fileName", file.getName());
                    fileInfo.put("filePath", file.getAbsolutePath());
                    fileInfo.put("fileSize", file.length());
                    fileInfo.put("lastModified", new java.util.Date(file.lastModified()));
                    fileList.add(fileInfo);
                }
            }

            // 按修改时间倒序排序
            fileList.sort((a, b) -> {
                java.util.Date dateA = (java.util.Date) a.get("lastModified");
                java.util.Date dateB = (java.util.Date) b.get("lastModified");
                return dateB.compareTo(dateA);
            });

            return ResponseUtil.success(fileList);
        } catch (Exception e) {
            log.error("获取备份文件列表失败", e);
            return ResponseUtil.error("查询失败: " + e.getMessage());
        }
    }
}
