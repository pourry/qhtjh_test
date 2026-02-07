-- 数据库备份记录表（可选）
CREATE TABLE IF NOT EXISTS `db_backup_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `file_name` varchar(255) NOT NULL COMMENT '备份文件名',
  `file_path` varchar(500) NOT NULL COMMENT '备份文件路径',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小（字节）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` varchar(20) DEFAULT 'SUCCESS' COMMENT '备份状态：SUCCESS-成功，FAILED-失败',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库备份记录表';
