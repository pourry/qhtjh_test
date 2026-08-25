-- 走马灯表
CREATE TABLE IF NOT EXISTS `carousel` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `title` varchar(100) DEFAULT NULL COMMENT '走马灯标题',
  `pictureLogic` varchar(255) DEFAULT NULL COMMENT '图片逻辑文件名',
  `picturePath` varchar(500) DEFAULT NULL COMMENT '图片存储路径',
  `linkUrl` varchar(500) DEFAULT NULL COMMENT '点击跳转的URL',
  `sort` int(11) DEFAULT 0 COMMENT '排序序号',
  `enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `createTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页走马灯表';
