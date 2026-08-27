-- =============================================
-- 消息中心数据库表结构
-- 创建日期: 2026-08-26
-- 说明: 包含聊天室、通知、反馈三张核心表
-- =============================================

-- ==================== 聊天室消息表 ====================
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `sender_id` VARCHAR(64) NOT NULL COMMENT '发送者用户ID',
  `sender_name` VARCHAR(128) NOT NULL COMMENT '发送者昵称',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `type` VARCHAR(32) NOT NULL DEFAULT 'text' COMMENT '消息类型: text-文本, system-系统消息',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_sender_id` (`sender_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_id_desc` (`id` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天室消息表';

-- 兼容已有库：若表已存在但缺 desc 索引，单独跑下面这行
-- ALTER TABLE `chat_message` ADD INDEX `idx_id_desc` (`id` DESC);

-- 初始化系统欢迎消息
INSERT INTO `chat_message` (`sender_id`, `sender_name`, `content`, `type`, `create_time`) 
VALUES ('system', '系统', '欢迎来到聊天室，请文明发言！', 'system', NOW());

-- ==================== 通知表 ====================
CREATE TABLE IF NOT EXISTS `notification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `title` VARCHAR(255) NOT NULL COMMENT '通知标题',
  `description` TEXT COMMENT '通知内容/描述',
  `type` VARCHAR(32) NOT NULL DEFAULT 'info' COMMENT '通知类型: info-信息, warning-警告, success-成功, announcement-公告',
  `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读: 0-未读, 1-已读',
  `receiver_id` VARCHAR(64) NOT NULL COMMENT '接收者用户ID',
  `publisher_id` VARCHAR(64) COMMENT '发布者用户ID',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_receiver_id` (`receiver_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 为已有用户创建一条系统通知（示例数据，可根据实际情况调整）
-- INSERT INTO `notification` (`title`, `description`, `type`, `is_read`, `receiver_id`, `publisher_id`, `create_time`) 
-- VALUES ('欢迎使用消息中心', '消息中心功能已上线，快来体验吧！', 'announcement', 0, '1', 'system', NOW());

-- ==================== 反馈表 ====================
CREATE TABLE IF NOT EXISTS `feedback` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `type` VARCHAR(32) NOT NULL DEFAULT 'other' COMMENT '反馈类型: bug-Bug报告, feature-功能建议, improvement-改进建议, other-其他问题',
  `title` VARCHAR(255) NOT NULL COMMENT '反馈标题',
  `description` TEXT NOT NULL COMMENT '反馈详细描述',
  `contact` VARCHAR(255) COMMENT '联系方式(邮箱或手机号)',
  `status` VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '反馈状态: pending-待处理, processing-处理中, resolved-已解决, closed-已关闭',
  `user_id` VARCHAR(64) NOT NULL COMMENT '提交者用户ID',
  `user_name` VARCHAR(128) COMMENT '提交者用户名',
  `reply` TEXT COMMENT '管理员回复内容',
  `handler_id` VARCHAR(64) COMMENT '处理者用户ID',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈表';

-- 示例反馈数据（可删除）
-- INSERT INTO `feedback` (`type`, `title`, `description`, `contact`, `status`, `user_id`, `user_name`, `reply`, `handler_id`, `create_time`, `update_time`) 
-- VALUES ('bug', '登录页面加载缓慢', '在网络环境较差时，登录页面加载时间超过10秒。', '', 'pending', '1', '张三', NULL, NULL, NOW(), NOW());
-- INSERT INTO `feedback` (`type`, `title`, `description`, `contact`, `status`, `user_id`, `user_name`, `reply`, `handler_id`, `create_time`, `update_time`) 
-- VALUES ('feature', '建议增加夜间模式', '希望能够增加夜间模式功能，保护眼睛。', '', 'processing', '2', '李四', '已将此需求加入排期。', 'admin', NOW(), NOW());