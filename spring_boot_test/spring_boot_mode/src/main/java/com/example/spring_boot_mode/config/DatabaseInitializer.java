package com.example.spring_boot_mode.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 数据库表初始化器
 * 应用启动后自动创建缺失的表和字段
 * 使用 ApplicationRunner 确保在所有 Bean 初始化完成后执行
 */
@Slf4j
@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(@Qualifier("modeDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            // 1. 创建 carousel 表（如果不存在）
            String createCarouselSql = "CREATE TABLE IF NOT EXISTS carousel (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "title varchar(100) DEFAULT NULL COMMENT '走马灯标题'," +
                    "pictureLogic varchar(255) DEFAULT NULL COMMENT '图片逻辑文件名'," +
                    "picturePath varchar(500) DEFAULT NULL COMMENT '图片存储路径'," +
                    "linkUrl varchar(500) DEFAULT NULL COMMENT '点击跳转的URL'," +
                    "sort int(11) DEFAULT 0 COMMENT '排序序号'," +
                    "enabled tinyint(1) DEFAULT 1 COMMENT '是否启用'," +
                    "createTime varchar(20) DEFAULT NULL COMMENT '创建时间'," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页走马灯表'";
            jdbcTemplate.execute(createCarouselSql);
            log.info("=== Carousel table initialized successfully ===");

            // 2. 创建 footer_section 表（如果不存在）
            String createFooterSectionSql = "CREATE TABLE IF NOT EXISTS footer_section (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "type varchar(50) NOT NULL COMMENT '板块类型：intro-简介、contact-联系方式、record-备案'," +
                    "title varchar(100) DEFAULT NULL COMMENT '板块标题'," +
                    "content text COMMENT '板块内容'," +
                    "sort int(11) DEFAULT 0 COMMENT '排序序号'," +
                    "enabled tinyint(1) DEFAULT 1 COMMENT '是否启用'," +
                    "createTime varchar(20) DEFAULT NULL COMMENT '创建时间'," +
                    "updateTime varchar(20) DEFAULT NULL COMMENT '更新时间'," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='底部内容板块表'";
            jdbcTemplate.execute(createFooterSectionSql);
            log.info("=== FooterSection table initialized successfully ===");

            // 3. 创建 footer_link 表（如果不存在）
            String createFooterLinkSql = "CREATE TABLE IF NOT EXISTS footer_link (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "name varchar(100) NOT NULL COMMENT '链接名称'," +
                    "url varchar(500) DEFAULT NULL COMMENT '链接地址'," +
                    "sort int(11) DEFAULT 0 COMMENT '排序序号'," +
                    "enabled tinyint(1) DEFAULT 1 COMMENT '是否启用'," +
                    "createTime varchar(20) DEFAULT NULL COMMENT '创建时间'," +
                    "updateTime varchar(20) DEFAULT NULL COMMENT '更新时间'," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='底部快速链接表'";
            jdbcTemplate.execute(createFooterLinkSql);
            log.info("=== FooterLink table initialized successfully ===");

            // 4. 检查并添加 sys_user 表的缺失字段
            addColumnIfNotExists("sys_user", "phone", "varchar(20) DEFAULT NULL COMMENT '手机号'");
            addColumnIfNotExists("sys_user", "birthday", "varchar(20) DEFAULT NULL COMMENT '生日'");
            addColumnIfNotExists("sys_user", "gender", "varchar(10) DEFAULT NULL COMMENT '性别'");
            addColumnIfNotExists("sys_user", "avatar", "varchar(500) DEFAULT NULL COMMENT '头像路径'");
            addColumnIfNotExists("sys_user", "createTime", "varchar(20) DEFAULT NULL COMMENT '创建时间'");
            addColumnIfNotExists("sys_user", "updateTime", "varchar(20) DEFAULT NULL COMMENT '更新时间'");
            log.info("=== SysUser table columns initialized successfully ===");

        } catch (Exception e) {
            log.error("=== Database initialization failed: {} ===", e.getMessage());
        }
    }

    /**
     * 如果列不存在则添加
     */
    private void addColumnIfNotExists(String tableName, String columnName, String columnDef) {
        try {
            // 检查列是否存在
            String checkSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, tableName, columnName);
            if (count == null || count == 0) {
                String alterSql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDef;
                jdbcTemplate.execute(alterSql);
                log.info("=== Added column '{}' to table '{}' ===", columnName, tableName);
            }
        } catch (Exception e) {
            log.warn("=== Failed to add column '{}': {} ===", columnName, e.getMessage());
        }
    }
}
