package com.example.spring_boot_mode.performance;

import com.example.spring_boot_mode.model.service.DatabaseBackupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库备份性能测试
 * 注意：性能测试默认禁用，需要时手动启用
 */
@SpringBootTest
@DisplayName("数据库备份性能测试")
public class DatabaseBackupPerformanceTest {

    @Autowired
    private DatabaseBackupService databaseBackupService;

    @Test
    @DisplayName("测试备份执行时间")
    @Disabled("性能测试，需要时手动启用")
    void testBackupExecutionTime() {
        try {
            long startTime = System.currentTimeMillis();
            
            String filePath = databaseBackupService.backupDatabase();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertNotNull(filePath, "备份文件路径不应为null");
            
            File backupFile = new File(filePath);
            long fileSize = backupFile.length();
            
            System.out.println("========== 性能测试结果 ==========");
            System.out.println("执行时间: " + duration + " ms (" + (duration / 1000.0) + " 秒)");
            System.out.println("文件大小: " + fileSize + " bytes (" + (fileSize / 1024.0) + " KB)");
            System.out.println("备份速度: " + (fileSize / duration) + " bytes/ms");
            System.out.println("================================");
            
            // 性能断言（根据实际情况调整）
            assertTrue(duration < 60000, "备份时间应该在60秒内完成");
            
        } catch (Exception e) {
            fail("性能测试失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试连续备份性能")
    @Disabled("性能测试，需要时手动启用")
    void testMultipleBackupsPerformance() {
        int backupCount = 3;
        long totalTime = 0;
        
        System.out.println("========== 连续备份性能测试 ==========");
        
        for (int i = 1; i <= backupCount; i++) {
            try {
                long startTime = System.currentTimeMillis();
                String filePath = databaseBackupService.backupDatabase();
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                totalTime += duration;
                
                System.out.println("第 " + i + " 次备份:");
                System.out.println("  执行时间: " + duration + " ms");
                System.out.println("  文件路径: " + filePath);
                
                // 删除测试文件
                new File(filePath).delete();
                
            } catch (Exception e) {
                fail("第 " + i + " 次备份失败: " + e.getMessage());
            }
        }
        
        double avgTime = totalTime / (double) backupCount;
        System.out.println("平均执行时间: " + avgTime + " ms");
        System.out.println("====================================");
    }

    @Test
    @DisplayName("测试内存使用情况")
    @Disabled("性能测试，需要时手动启用")
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        // 执行GC
        System.gc();
        
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        try {
            String filePath = databaseBackupService.backupDatabase();
            assertNotNull(filePath, "备份文件路径不应为null");
            
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = memoryAfter - memoryBefore;
            
            System.out.println("========== 内存使用测试 ==========");
            System.out.println("备份前内存: " + (memoryBefore / 1024 / 1024) + " MB");
            System.out.println("备份后内存: " + (memoryAfter / 1024 / 1024) + " MB");
            System.out.println("内存增长: " + (memoryUsed / 1024 / 1024) + " MB");
            System.out.println("================================");
            
            // 清理测试文件
            new File(filePath).delete();
            
        } catch (Exception e) {
            fail("内存测试失败: " + e.getMessage());
        }
    }
}
