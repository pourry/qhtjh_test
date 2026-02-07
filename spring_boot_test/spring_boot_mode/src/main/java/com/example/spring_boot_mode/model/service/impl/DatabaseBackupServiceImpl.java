package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.model.dao.DatabaseBackupDao;
import com.example.spring_boot_mode.model.entity.DatabaseBackup;
import com.example.spring_boot_mode.model.service.DatabaseBackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据库备份服务实现
 */
@Service
public class DatabaseBackupServiceImpl implements DatabaseBackupService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseBackupServiceImpl.class);

    @Autowired
    private DatabaseBackupDao databaseBackupDao;

    @Autowired
    @Qualifier("modeDataSource")
    private DataSource dataSource;

    @Value("${spring.datasource.mode.jdbc-url}")
    private String jdbcUrl;

    @Override
    public String backupDatabase() throws Exception {
        // 生成文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String fileName = "primary_db_" + dateStr + ".sql";

        // 获取项目根目录下的 resources/sql 目录
        String projectPath = System.getProperty("user.dir");
        String backupDir = projectPath + File.separator + "src" + File.separator + "main" 
                         + File.separator + "resources" + File.separator + "sql";
        
        // 确保目录存在
        File dir = new File(backupDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = backupDir + File.separator + fileName;
        File sqlFile = new File(filePath);

        log.info("开始备份数据库，文件路径: {}", filePath);

        try (Connection conn = dataSource.getConnection();
             BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFile))) {

            // 获取数据库名
            String databaseName = getDatabaseName(jdbcUrl);
            
            // 写入文件头
            writer.write("-- =============================================\n");
            writer.write("-- Database Backup\n");
            writer.write("-- Database: " + databaseName + "\n");
            writer.write("-- Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            writer.write("-- =============================================\n\n");
            writer.write("SET FOREIGN_KEY_CHECKS=0;\n\n");

            // 获取所有表
            List<String> tables = databaseBackupDao.getAllTables(databaseName);
            log.info("找到 {} 个表需要备份", tables.size());

            for (String tableName : tables) {
                log.info("正在备份表: {}", tableName);
                
                // 写入表结构
                writer.write("-- ----------------------------\n");
                writer.write("-- Table structure for " + tableName + "\n");
                writer.write("-- ----------------------------\n");
                writer.write("DROP TABLE IF EXISTS `" + tableName + "`;\n");
                
                String createTableSql = getCreateTableSql(conn, tableName);
                writer.write(createTableSql + ";\n\n");

                // 写入表数据
                writer.write("-- ----------------------------\n");
                writer.write("-- Records of " + tableName + "\n");
                writer.write("-- ----------------------------\n");
                
                exportTableData(conn, tableName, writer);
                writer.write("\n");
            }

            writer.write("SET FOREIGN_KEY_CHECKS=1;\n");
            writer.flush();

            log.info("数据库备份完成，文件大小: {} bytes", sqlFile.length());

            // 保存备份记录（可选，如果有备份记录表的话）
            // saveBackupRecord(fileName, filePath, sqlFile.length());

            return filePath;

        } catch (Exception e) {
            log.error("数据库备份失败", e);
            throw new Exception("数据库备份失败: " + e.getMessage());
        }
    }

    /**
     * 从 JDBC URL 中提取数据库名
     */
    private String getDatabaseName(String jdbcUrl) {
        // jdbc:mysql://localhost:3306/test_all?serverTimezone=UTC
        String[] parts = jdbcUrl.split("/");
        if (parts.length > 3) {
            String dbPart = parts[3];
            if (dbPart.contains("?")) {
                return dbPart.substring(0, dbPart.indexOf("?"));
            }
            return dbPart;
        }
        return "unknown";
    }

    /**
     * 获取表的创建语句
     */
    private String getCreateTableSql(Connection conn, String tableName) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE `" + tableName + "`")) {
            if (rs.next()) {
                return rs.getString(2);
            }
        }
        return "";
    }

    /**
     * 导出表数据
     */
    private void exportTableData(Connection conn, String tableName, BufferedWriter writer) throws Exception {
        String sql = "SELECT * FROM `" + tableName + "`";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 如果表中有数据
            if (rs.next()) {
                do {
                    StringBuilder insertSql = new StringBuilder();
                    insertSql.append("INSERT INTO `").append(tableName).append("` VALUES (");

                    for (int i = 1; i <= columnCount; i++) {
                        Object value = rs.getObject(i);
                        
                        if (value == null) {
                            insertSql.append("NULL");
                        } else if (value instanceof String || value instanceof Date || value instanceof Timestamp) {
                            // 字符串和日期类型需要加引号，并转义特殊字符
                            String strValue = value.toString();
                            strValue = strValue.replace("\\", "\\\\")
                                             .replace("'", "\\'")
                                             .replace("\n", "\\n")
                                             .replace("\r", "\\r");
                            insertSql.append("'").append(strValue).append("'");
                        } else {
                            insertSql.append(value);
                        }

                        if (i < columnCount) {
                            insertSql.append(", ");
                        }
                    }

                    insertSql.append(");\n");
                    writer.write(insertSql.toString());
                    
                } while (rs.next());
            }
        }
    }

    /**
     * 保存备份记录（如果需要记录备份历史）
     */
    private void saveBackupRecord(String fileName, String filePath, long fileSize) {
        try {
            DatabaseBackup backup = new DatabaseBackup();
            backup.setFileName(fileName);
            backup.setFilePath(filePath);
            backup.setFileSize(fileSize);
            backup.setCreateTime(new Date());
            backup.setStatus("SUCCESS");
            backup.setRemark("自动备份");
            
            databaseBackupDao.insertBackupRecord(backup);
        } catch (Exception e) {
            log.error("保存备份记录失败", e);
        }
    }

    @Override
    public List<DatabaseBackup> getBackupList() {
        try {
            return databaseBackupDao.selectBackupList();
        } catch (Exception e) {
            log.error("查询备份记录失败", e);
            return new ArrayList<>();
        }
    }
}
