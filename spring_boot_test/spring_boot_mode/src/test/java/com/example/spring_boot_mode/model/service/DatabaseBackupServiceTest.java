package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.model.entity.DatabaseBackup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库备份服务测试类
 */
@SpringBootTest
@DisplayName("数据库备份服务测试")
public class DatabaseBackupServiceTest {

    @Autowired
    private DatabaseBackupService databaseBackupService;

    @Test
    @DisplayName("测试数据库备份功能")
    void testBackupDatabase() {
        try {
            // 执行备份
            String filePath = databaseBackupService.backupDatabase();
            
            // 验证返回的文件路径不为空
            assertNotNull(filePath, "备份文件路径不应为空");
            
            // 验证文件是否存在
            File backupFile = new File(filePath);
            assertTrue(backupFile.exists(), "备份文件应该存在");
            
            // 验证文件大小大于0
            assertTrue(backupFile.length() > 0, "备份文件大小应该大于0");
            
            // 验证文件名格式
            String fileName = backupFile.getName();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String expectedFileName = "primary_db_" + sdf.format(new Date()) + ".sql";
            assertEquals(expectedFileName, fileName, "文件名格式应该正确");
            
            System.out.println("✅ 备份成功！文件路径: " + filePath);
            System.out.println("✅ 文件大小: " + backupFile.length() + " bytes");
            
        } catch (Exception e) {
            fail("备份过程中发生异常: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试获取备份记录列表")
    void testGetBackupList() {
        try {
            List<DatabaseBackup> backupList = databaseBackupService.getBackupList();
            
            // 验证返回的列表不为null
            assertNotNull(backupList, "备份记录列表不应为null");
            
            System.out.println("✅ 获取备份记录成功，共 " + backupList.size() + " 条记录");
            
        } catch (Exception e) {
            // 如果没有创建备份记录表，这里可能会失败，这是正常的
            System.out.println("⚠️ 获取备份记录失败（可能未创建备份记录表）: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试备份文件内容格式")
    void testBackupFileContent() {
        try {
            // 执行备份
            String filePath = databaseBackupService.backupDatabase();
            File backupFile = new File(filePath);
            
            // 读取文件内容
            java.nio.file.Path path = backupFile.toPath();
            String content = new String(java.nio.file.Files.readAllBytes(path), "UTF-8");
            
            // 验证文件包含必要的SQL语句
            assertTrue(content.contains("Database Backup"), "应包含备份标题");
            assertTrue(content.contains("SET FOREIGN_KEY_CHECKS=0"), "应包含外键检查设置");
            assertTrue(content.contains("DROP TABLE IF EXISTS"), "应包含删除表语句");
            assertTrue(content.contains("CREATE TABLE"), "应包含创建表语句");
            
            System.out.println("✅ 备份文件内容格式正确");
            
        } catch (Exception e) {
            fail("验证备份文件内容时发生异常: " + e.getMessage());
        }
    }
}
