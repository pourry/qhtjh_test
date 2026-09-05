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

            // 为 reminder 表添加 is_read 列（如果不存在）
            addColumnIfNotExists("reminder", "is_read", "tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读, 1-已读' AFTER is_open");
            // 为 reminder 表添加 repeat_type 列（如果不存在）
            addColumnIfNotExists("reminder", "repeat_type", "varchar(20) NOT NULL DEFAULT 'none' COMMENT '重复类型：none-不重复, hourly-每小时, daily-每天, weekly-每周, monthly-每月, yearly-每年, custom-自定义' AFTER is_read");
            // 为 reminder 表添加 repeat_interval 列（如果不存在）
            addColumnIfNotExists("reminder", "repeat_interval", "int DEFAULT NULL COMMENT '自定义重复间隔（分钟），仅当repeat_type=custom时有效' AFTER repeat_type");
            // 为 reminder 表添加 next_remind_time 列（如果不存在）
            addColumnIfNotExists("reminder", "next_remind_time", "datetime DEFAULT NULL COMMENT '下次提醒时间（周期性提醒触发后自动计算）' AFTER repeat_interval");
            // 为 reminder 表添加 repeat_end_time 列（如果不存在）
            addColumnIfNotExists("reminder", "repeat_end_time", "datetime DEFAULT NULL COMMENT '周期结束时间（为空表示无限重复）' AFTER next_remind_time");
            // 为 reminder 表添加 alias 列（如果不存在）
            addColumnIfNotExists("reminder", "alias", "varchar(255) DEFAULT NULL COMMENT '目标别名（冗余字段，方便展示）' AFTER target_name");
            // 为 reminder 表添加 address 列（如果不存在）
            addColumnIfNotExists("reminder", "address", "varchar(500) DEFAULT NULL COMMENT '目标地址（冗余字段，方便展示）' AFTER alias");

            // 9. 创建 other_collection_type 表（综合收藏类型，按用户隔离）
            String createOctSql = "CREATE TABLE IF NOT EXISTS other_collection_type (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "user_id varchar(64) NOT NULL COMMENT '归属用户ID'," +
                    "type_value varchar(50) NOT NULL COMMENT '类型标识'," +
                    "label varchar(50) NOT NULL COMMENT '类型标签'," +
                    "icon varchar(50) DEFAULT NULL COMMENT '图标名称'," +
                    "color varchar(20) DEFAULT NULL COMMENT '颜色HEX值'," +
                    "sort int DEFAULT 0 COMMENT '排序'," +
                    "create_time datetime DEFAULT NULL COMMENT '创建时间'," +
                    "PRIMARY KEY (id)," +
                    "KEY idx_user_id (user_id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='综合收藏类型表'";
            jdbcTemplate.execute(createOctSql);
            log.info("=== OtherCollectionType table initialized successfully ===");

            // 10. 创建 other_collection 表（综合收藏）
            String createOcSql = "CREATE TABLE IF NOT EXISTS other_collection (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "user_id varchar(64) NOT NULL COMMENT '收藏者用户ID'," +
                    "type_value varchar(50) DEFAULT NULL COMMENT '类型标识'," +
                    "title varchar(255) NOT NULL COMMENT '标题'," +
                    "link_url varchar(500) DEFAULT NULL COMMENT '链接'," +
                    "picture_url varchar(500) DEFAULT NULL COMMENT '封面图路径'," +
                    "note text COMMENT '备注'," +
                    "tags varchar(500) DEFAULT NULL COMMENT '标签JSON数组字符串'," +
                    "status varchar(20) DEFAULT 'wish' COMMENT '状态: wish/doing/done'," +
                    "progress int DEFAULT 0 COMMENT '进度 0-100'," +
                    "pinned tinyint(1) DEFAULT 0 COMMENT '是否置顶'," +
                    "share tinyint(1) DEFAULT 0 COMMENT '是否分享到首页'," +
                    "share_time datetime DEFAULT NULL COMMENT '分享时间'," +
                    "create_time datetime DEFAULT NULL COMMENT '创建时间'," +
                    "update_time datetime DEFAULT NULL COMMENT '更新时间'," +
                    "PRIMARY KEY (id)," +
                    "KEY idx_user_id (user_id)," +
                    "KEY idx_type (type_value)," +
                    "KEY idx_share (share)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='综合收藏表'";
            jdbcTemplate.execute(createOcSql);
            log.info("=== OtherCollection table initialized successfully ===");

            // ========== 角色权限表初始化 ==========

            // 10. 创建 sys_role 表
            String createRoleSql = "CREATE TABLE IF NOT EXISTS sys_role (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "roleName varchar(100) NOT NULL COMMENT '角色名称'," +
                    "roleCode varchar(50) NOT NULL COMMENT '角色编码（唯一标识）'," +
                    "description varchar(500) DEFAULT NULL COMMENT '角色描述'," +
                    "isBuiltin tinyint(1) DEFAULT 0 COMMENT '是否内置：0-否, 1-是'," +
                    "createTime varchar(20) DEFAULT NULL COMMENT '创建时间'," +
                    "updateTime varchar(20) DEFAULT NULL COMMENT '更新时间'," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE KEY uk_roleCode (roleCode)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表'";
            jdbcTemplate.execute(createRoleSql);
            log.info("=== SysRole table initialized successfully ===");

            // 11. 创建 sys_permission 表
            String createPermissionSql = "CREATE TABLE IF NOT EXISTS sys_permission (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "permissionName varchar(100) NOT NULL COMMENT '权限名称'," +
                    "permissionCode varchar(100) NOT NULL COMMENT '权限编码（唯一标识）'," +
                    "path varchar(255) DEFAULT NULL COMMENT '对应路由路径'," +
                    "groupName varchar(50) DEFAULT NULL COMMENT '所属分组'," +
                    "description varchar(500) DEFAULT NULL COMMENT '描述'," +
                    "isBuiltin tinyint(1) DEFAULT 0 COMMENT '是否内置：0-否, 1-是'," +
                    "sort int(11) DEFAULT 0 COMMENT '排序序号'," +
                    "createTime varchar(20) DEFAULT NULL COMMENT '创建时间'," +
                    "updateTime varchar(20) DEFAULT NULL COMMENT '更新时间'," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE KEY uk_permissionCode (permissionCode)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限/页面表'";
            jdbcTemplate.execute(createPermissionSql);
            log.info("=== SysPermission table initialized successfully ===");

            // 12. 创建 sys_role_permission 表
            String createRolePermSql = "CREATE TABLE IF NOT EXISTS sys_role_permission (" +
                    "id varchar(64) NOT NULL COMMENT '主键ID'," +
                    "roleId varchar(64) NOT NULL COMMENT '角色ID'," +
                    "permissionId varchar(64) NOT NULL COMMENT '权限ID'," +
                    "createTime varchar(20) DEFAULT NULL COMMENT '创建时间'," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE KEY uk_role_perm (roleId, permissionId)," +
                    "KEY idx_permissionId (permissionId)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限关联表'";
            jdbcTemplate.execute(createRolePermSql);
            log.info("=== SysRolePermission table initialized successfully ===");

            // 为 sys_user 表添加 roleId 字段
            addColumnIfNotExists("sys_user", "roleId", "varchar(64) DEFAULT NULL COMMENT '用户角色ID'");
            log.info("=== SysUser roleId column initialized successfully ===");

            // 初始化内置角色和权限数据（幂等，通过 NOT EXISTS 检查）
            initDefaultRoles();
            initDefaultPermissions();
            initDefaultRolePermissions();
            log.info("=== Default role/permission data initialized ===");

            // ========== animation 表新增字段（ACG 收藏扩展） ==========
            // 说明：全部不加 AFTER，让 MySQL 直接追加到表末尾，避免 AFTER 目标列还不存在导致的错误
            // 通用字段
            addColumnIfNotExists("animation", "type", "varchar(20) DEFAULT 'animation' COMMENT '类型: animation/comic/novel/game'");
            addColumnIfNotExists("animation", "rating", "DECIMAL(3,1) DEFAULT NULL COMMENT '评分 0-10'");
            addColumnIfNotExists("animation", "tags", "varchar(200) DEFAULT NULL COMMENT '标签,逗号分隔'");
            // 动画专属
            addColumnIfNotExists("animation", "episodes", "int DEFAULT NULL COMMENT '总集数(动画)'");
            addColumnIfNotExists("animation", "studio", "varchar(100) DEFAULT NULL COMMENT '制作公司(动画)'");
            addColumnIfNotExists("animation", "voice_actors", "varchar(300) DEFAULT NULL COMMENT '声优(动画)'");
            addColumnIfNotExists("animation", "source", "varchar(50) DEFAULT NULL COMMENT '原作来源(动画): 漫改/轻改/原创/游戏改'");
            // 漫画专属
            addColumnIfNotExists("animation", "chapters", "int DEFAULT NULL COMMENT '总话数(漫画)'");
            addColumnIfNotExists("animation", "comic_author", "varchar(100) DEFAULT NULL COMMENT '作者(漫画)'");
            addColumnIfNotExists("animation", "publisher", "varchar(100) DEFAULT NULL COMMENT '出版社(漫画)'");
            addColumnIfNotExists("animation", "serialization", "varchar(50) DEFAULT NULL COMMENT '连载周期(漫画)'");
            // 小说专属
            addColumnIfNotExists("animation", "word_count", "int DEFAULT NULL COMMENT '总字数(千字,小说)'");
            addColumnIfNotExists("animation", "novel_author", "varchar(100) DEFAULT NULL COMMENT '作者(小说)'");
            addColumnIfNotExists("animation", "platform", "varchar(100) DEFAULT NULL COMMENT '平台(小说)'");
            addColumnIfNotExists("animation", "category", "varchar(50) DEFAULT NULL COMMENT '分类(小说)'");
            // 游戏专属
            addColumnIfNotExists("animation", "game_platform", "varchar(100) DEFAULT NULL COMMENT '游戏平台'");
            addColumnIfNotExists("animation", "developer", "varchar(100) DEFAULT NULL COMMENT '开发商(游戏)'");
            addColumnIfNotExists("animation", "genre", "varchar(50) DEFAULT NULL COMMENT '游戏类型'");
            addColumnIfNotExists("animation", "hours_played", "int DEFAULT NULL COMMENT '游玩小时数(游戏)'");

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

    /**
     * 初始化默认角色（内置，不可删除）
     */
    private void initDefaultRoles() {
        String now = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 管理员角色
        String insertAdminRole = "INSERT INTO sys_role (id, roleName, roleCode, description, isBuiltin, createTime, updateTime) " +
                "SELECT 'role_admin', '管理员', 'admin', '拥有所有页面访问权限', 1, '" + now + "', '" + now + "' " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE roleCode = 'admin')";
        jdbcTemplate.execute(insertAdminRole);

        // 普通用户角色
        String insertUserRole = "INSERT INTO sys_role (id, roleName, roleCode, description, isBuiltin, createTime, updateTime) " +
                "SELECT 'role_user', '普通用户', 'user', '基础页面访问权限，无法访问管理功能', 1, '" + now + "', '" + now + "' " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE roleCode = 'user')";
        jdbcTemplate.execute(insertUserRole);

        // 访客角色（未登录时）
        String insertGuestRole = "INSERT INTO sys_role (id, roleName, roleCode, description, isBuiltin, createTime, updateTime) " +
                "SELECT 'role_guest', '访客', 'guest', '未登录状态下的只读权限', 1, '" + now + "', '" + now + "' " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE roleCode = 'guest')";
        jdbcTemplate.execute(insertGuestRole);
    }

    /**
     * 初始化默认权限/页面（内置）
     */
    private void initDefaultPermissions() {
        String now = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 首页
        insertPermissionIfNotExists("perm_home", "首页", "page:home", "/home", "首页", "系统首页", 0, now);

        // 收藏夹相关
        insertPermissionIfNotExists("perm_favorites", "收藏夹", "page:favorites", "/favorites", "收藏夹", "收藏夹入口", 1, now);
        insertPermissionIfNotExists("perm_twod_animation", "动画收藏", "page:favorites:animation", "/twoDimensions/animation", "收藏夹", "二次元-动画", 2, now);
        insertPermissionIfNotExists("perm_twod_comic", "漫画收藏", "page:favorites:comic", "/twoDimensions/comic", "收藏夹", "二次元-漫画", 3, now);
        insertPermissionIfNotExists("perm_twod_novel", "小说收藏", "page:favorites:novel", "/twoDimensions/novel", "收藏夹", "二次元-小说", 4, now);
        insertPermissionIfNotExists("perm_twod_game", "游戏收藏", "page:favorites:game", "/twoDimensions/game", "收藏夹", "二次元-游戏", 5, now);
        insertPermissionIfNotExists("perm_url_collect", "网站收藏", "page:favorites:url", "/oneDimensions/uRLcollect", "收藏夹", "一次元-网站收藏", 7, now);
        insertPermissionIfNotExists("perm_other_collect", "综合收藏", "page:favorites:other", "/otherDimensions", "收藏夹", "书籍/电影/音乐/名言等综合收藏", 9, now);

        // 清理已废弃的三次元/四次元权限（关联和主表）
        try {
            jdbcTemplate.execute("DELETE FROM sys_role_permission WHERE permissionId IN ('perm_threed_tv', 'perm_fourdimensions', 'perm_user_apply')");
            jdbcTemplate.execute("DELETE FROM sys_permission WHERE id IN ('perm_threed_tv', 'perm_fourdimensions', 'perm_user_apply')");
            log.info("=== Cleaned up deprecated permissions ===");
        } catch (Exception e) {
            log.warn("Cleanup deprecated permissions warn: {}", e.getMessage());
        }

        // 我的相关 - 基础（所有登录用户可用）
        insertPermissionIfNotExists("perm_user_self", "我的", "page:self", "/userSelf", "我的", "我的入口", 10, now);
        insertPermissionIfNotExists("perm_user_my_msg", "我的信息", "page:self:msg", "/userSelf/myMsg", "我的", "个人信息", 11, now);

        // 我的相关 - 管理功能（仅管理员）
        insertPermissionIfNotExists("perm_user_carousel", "走马灯管理", "page:self:carousel", "/userSelf/carousel", "管理", "走马灯控制", 20, now);
        insertPermissionIfNotExists("perm_user_footer", "底部内容管理", "page:self:footer", "/userSelf/footer", "管理", "底部内容控制", 21, now);
        insertPermissionIfNotExists("perm_user_basic_config", "基本配置", "page:self:basicConfig", "/userSelf/basicConfig", "管理", "系统基本配置", 22, now);
        insertPermissionIfNotExists("perm_user_total_feedback", "总反馈管理", "page:self:totalFeedback", "/userSelf/totalFeedback", "管理", "反馈管理", 23, now);
        insertPermissionIfNotExists("perm_user_publish_notification", "发布通知", "page:self:publishNotification", "/userSelf/publishNotification", "管理", "通知发布", 24, now);
        insertPermissionIfNotExists("perm_user_role_manage", "角色权限管理", "page:self:rolePermission", "/userSelf/rolePermission", "管理", "角色权限配置", 25, now);

        // 消息中心
        insertPermissionIfNotExists("perm_message_center", "消息中心", "page:messageCenter", "/messageCenter", "消息", "消息中心", 30, now);
    }

    /**
     * 初始化默认角色-权限关联
     */
    private void initDefaultRolePermissions() {
        String now = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 管理员角色：拥有所有权限
        assignRoleAllPermissions("role_admin", now);

        // 普通用户角色：只有基础页面（首页、收藏夹全部、我的基础、消息中心）
        String[] userPerms = {
                "perm_home", "perm_favorites", "perm_twod_animation", "perm_twod_comic",
                "perm_twod_novel", "perm_twod_game", "perm_url_collect",
                "perm_other_collect", "perm_user_self", "perm_user_my_msg",
                "perm_message_center"
        };
        assignRoleSpecificPermissions("role_user", userPerms, now);

        // 访客角色：只有首页（未登录状态下前端不会进入除首页外的页面，但保留配置）
        String[] guestPerms = {"perm_home"};
        assignRoleSpecificPermissions("role_guest", guestPerms, now);
    }

    /**
     * 插入权限（如果不存在）
     */
    private void insertPermissionIfNotExists(String id, String name, String code, String path, String group, String desc, int sort, String now) {
        String sql = "INSERT INTO sys_permission (id, permissionName, permissionCode, path, groupName, description, isBuiltin, sort, createTime, updateTime) " +
                "SELECT '" + id + "', '" + name + "', '" + code + "', '" + path + "', '" + group + "', '" + desc + "', 1, " + sort + ", '" + now + "', '" + now + "' " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permissionCode = '" + code + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.warn("Init permission {} warn: {}", code, e.getMessage());
        }
    }

    /**
     * 为角色分配所有权限
     */
    private void assignRoleAllPermissions(String roleId, String now) {
        // 查询所有已存在的权限ID并关联
        String assignAll = "INSERT INTO sys_role_permission (id, roleId, permissionId, createTime) " +
                "SELECT CONCAT('rp_', p.id), '" + roleId + "', p.id, '" + now + "' " +
                "FROM sys_permission p " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_role_permission rp WHERE rp.roleId = '" + roleId + "' AND rp.permissionId = p.id)";
        try {
            jdbcTemplate.execute(assignAll);
        } catch (Exception e) {
            log.warn("Assign all permissions to role {} warn: {}", roleId, e.getMessage());
        }
    }

    /**
     * 为角色分配指定权限
     */
    private void assignRoleSpecificPermissions(String roleId, String[] permIds, String now) {
        for (String permId : permIds) {
            String assign = "INSERT INTO sys_role_permission (id, roleId, permissionId, createTime) " +
                    "SELECT CONCAT('rp_', '" + roleId + "', '_', '" + permId + "'), '" + roleId + "', '" + permId + "', '" + now + "' " +
                    "WHERE EXISTS (SELECT 1 FROM sys_permission WHERE id = '" + permId + "') " +
                    "AND NOT EXISTS (SELECT 1 FROM sys_role_permission rp WHERE rp.roleId = '" + roleId + "' AND rp.permissionId = '" + permId + "')";
            try {
                jdbcTemplate.execute(assign);
            } catch (Exception e) {
                log.warn("Assign permission {} to role {} warn: {}", permId, roleId, e.getMessage());
            }
        }
    }
}
