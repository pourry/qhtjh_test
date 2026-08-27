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
                    "objectFit varchar(50) DEFAULT 'cover' COMMENT '图片填充样式'," +
                    "linkTarget varchar(20) DEFAULT 'blank' COMMENT '跳转方式：blank-新窗口、self-本页面、router-路由跳转'," +
                    "createTime varchar(20) DEFAULT NULL COMMENT '创建时间'," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页走马灯表'";
            jdbcTemplate.execute(createCarouselSql);
            // 为已存在的 carousel 表添加 objectFit 字段
            addColumnIfNotExists("carousel", "objectFit", "varchar(50) DEFAULT 'cover' COMMENT '图片填充样式'");
            // 为已存在的 carousel 表添加 linkTarget 字段
            addColumnIfNotExists("carousel", "linkTarget", "varchar(20) DEFAULT 'blank' COMMENT '跳转方式：blank-新窗口、self-本页面、router-路由跳转'");
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
            addColumnIfNotExists("sys_user", "theme", "varchar(50) DEFAULT 'blue' COMMENT '用户主题'");
            addColumnIfNotExists("sys_user", "captchaEnabled", "tinyint(1) DEFAULT 0 COMMENT '登录验证码开关：0-关闭，1-启用'");
            addColumnIfNotExists("sys_user", "createTime", "varchar(20) DEFAULT NULL COMMENT '创建时间'");
            addColumnIfNotExists("sys_user", "updateTime", "varchar(20) DEFAULT NULL COMMENT '更新时间'");
            addColumnIfNotExists("sys_user", "onlineStatus", "tinyint(1) DEFAULT 0 COMMENT '在线状态：0-离线，1-在线'");
            log.info("=== SysUser table columns initialized successfully ===");

            // 5. 创建 chat_message 表（如果不存在）
            String createChatMessageSql = "CREATE TABLE IF NOT EXISTS chat_message (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID'," +
                    "sender_id VARCHAR(64) NOT NULL COMMENT '发送者用户ID'," +
                    "sender_name VARCHAR(128) NOT NULL COMMENT '发送者昵称'," +
                    "content TEXT NOT NULL COMMENT '消息内容'," +
                    "type VARCHAR(32) NOT NULL DEFAULT 'text' COMMENT '消息类型: text-文本, system-系统消息'," +
                    "create_time DATETIME NOT NULL COMMENT '创建时间'," +
                    "PRIMARY KEY (id)," +
                    "KEY idx_sender_id (sender_id)," +
                    "KEY idx_create_time (create_time)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天室消息表'";
            jdbcTemplate.execute(createChatMessageSql);
            log.info("=== ChatMessage table initialized successfully ===");

            // 初始化系统欢迎消息
            String initChatSql = "INSERT INTO chat_message (sender_id, sender_name, content, type, create_time) " +
                    "SELECT 'system', '系统', '欢迎来到聊天室，请文明发言！', 'system', NOW() " +
                    "WHERE NOT EXISTS (SELECT 1 FROM chat_message WHERE sender_id = 'system' AND type = 'system')";
            jdbcTemplate.execute(initChatSql);

            // 6. 创建 notification 表（如果不存在）
            String createNotificationSql = "CREATE TABLE IF NOT EXISTS notification (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID'," +
                    "title VARCHAR(255) NOT NULL COMMENT '通知标题'," +
                    "description TEXT COMMENT '通知内容/描述'," +
                    "type VARCHAR(32) NOT NULL DEFAULT 'info' COMMENT '通知类型: info-信息, warning-警告, success-成功, announcement-公告'," +
                    "is_read TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读: 0-未读, 1-已读'," +
                    "receiver_id VARCHAR(64) NOT NULL COMMENT '接收者用户ID'," +
                    "publisher_id VARCHAR(64) COMMENT '发布者用户ID'," +
                    "create_time DATETIME NOT NULL COMMENT '创建时间'," +
                    "PRIMARY KEY (id)," +
                    "KEY idx_receiver_id (receiver_id)," +
                    "KEY idx_is_read (is_read)," +
                    "KEY idx_create_time (create_time)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表'";
            jdbcTemplate.execute(createNotificationSql);
            log.info("=== Notification table initialized successfully ===");
            
            // 为 notification 表添加 batch_id 列（如果不存在）
            addColumnIfNotExists("notification", "batch_id", "VARCHAR(64) COMMENT '批次ID（同一批发布的通知共享同一个批次ID）' AFTER id");
            try { jdbcTemplate.execute("ALTER TABLE notification ADD INDEX idx_batch_id (batch_id)"); } catch (Exception e) { log.warn("Index idx_batch_id may already exist: {}", e.getMessage()); }

            // 7. 创建 feedback 表（如果不存在）
            String createFeedbackSql = "CREATE TABLE IF NOT EXISTS feedback (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT COMMENT '反馈ID'," +
                    "type VARCHAR(32) NOT NULL DEFAULT 'other' COMMENT '反馈类型: bug-Bug报告, feature-功能建议, improvement-改进建议, other-其他问题'," +
                    "title VARCHAR(255) NOT NULL COMMENT '反馈标题'," +
                    "description TEXT NOT NULL COMMENT '反馈详细描述'," +
                    "contact VARCHAR(255) COMMENT '联系方式(邮箱或手机号)'," +
                    "status VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '反馈状态: pending-待处理, processing-处理中, resolved-已解决, closed-已关闭'," +
                    "user_id VARCHAR(64) NOT NULL COMMENT '提交者用户ID'," +
                    "user_name VARCHAR(128) COMMENT '提交者用户名'," +
                    "reply TEXT COMMENT '管理员回复内容'," +
                    "handler_id VARCHAR(64) COMMENT '处理者用户ID'," +
                    "create_time DATETIME NOT NULL COMMENT '创建时间'," +
                    "update_time DATETIME NOT NULL COMMENT '更新时间'," +
                    "PRIMARY KEY (id)," +
                    "KEY idx_user_id (user_id)," +
                    "KEY idx_status (status)," +
                    "KEY idx_type (type)," +
                    "KEY idx_create_time (create_time)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈表'";
            jdbcTemplate.execute(createFeedbackSql);
            log.info("=== Feedback table initialized successfully ===");
            
            // 为 feedback 表添加 images 列（如果不存在）
            addColumnIfNotExists("feedback", "images", "TEXT COMMENT '附件图片路径列表(JSON数组字符串)' AFTER update_time");

            // 4. 创建 reminder 表（如果不存在）
            String createReminderSql = "CREATE TABLE IF NOT EXISTS reminder (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "target_type varchar(20) NOT NULL COMMENT '目标类型：animation/comic/novel/game'," +
                    "target_id varchar(64) NOT NULL COMMENT '目标ID'," +
                    "target_name varchar(255) COMMENT '目标名称'," +
                    "user_id varchar(64) NOT NULL COMMENT '用户ID'," +
                    "remind_time datetime NOT NULL COMMENT '提醒时间'," +
                    "remind_msg varchar(500) COMMENT '自定义提醒消息'," +
                    "status varchar(32) NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待触发, triggered-已触发, cancelled-已取消'," +
                    "trigger_time datetime COMMENT '实际触发时间'," +
                    "is_open tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否开启：0-关闭, 1-开启'," +
                    "create_time datetime NOT NULL COMMENT '创建时间'," +
                    "update_time datetime NOT NULL COMMENT '更新时间'," +
                    "PRIMARY KEY (id)," +
                    "KEY idx_user_id (user_id)," +
                    "KEY idx_target (target_type, target_id)," +
                    "KEY idx_status (status)," +
                    "KEY idx_remind_time (remind_time)," +
                    "KEY idx_is_open (is_open)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户提醒表（独立于系统通知）'";
            jdbcTemplate.execute(createReminderSql);
            log.info("=== Reminder table initialized successfully ===");

        } catch (Exception e) {
            log.error("=== Database initialization failed: {} ===", e.getMessage());
        }
    }

    /**
     * 如果列不存在则添加
     */
    private void addColumnIfNotExists(String tableName, String columnName, String columnDef) {
        try {
            // 直接尝试添加列（如果已存在会报错，被 catch 忽略）
            String alterSql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDef;
            jdbcTemplate.execute(alterSql);
            log.info("=== Added column '{}' to table '{}' ===", columnName, tableName);
        } catch (Exception e) {
            // 列已存在或其他错误，记录警告但不中断
            log.warn("=== Column '{}' in table '{}' check result: {} ===", columnName, tableName, e.getMessage());
        }
    }
}
