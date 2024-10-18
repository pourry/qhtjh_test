/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80037
Source Host           : localhost:3306
Source Database       : test_all

Target Server Type    : MYSQL
Target Server Version : 80037
File Encoding         : 65001

Date: 2024-10-18 17:18:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for animation
-- ----------------------------
DROP TABLE IF EXISTS `animation`;
CREATE TABLE `animation` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `hasend` varchar(255) DEFAULT NULL,
  `pictureURL` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `sscollector` varchar(64) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  `hasendLabel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of animation
-- ----------------------------
INSERT INTO `animation` VALUES ('05aa0bab-1698-4bdb-a98f-72f5fe40930c', 'Re：从零开始的异世界生活 第三季', 'https://www.agedm.org/detail/20240145', 'Re：从零开始的异世界生活 第三季', 'serialize', null, null, '001', '2024-10-17 16:32:28', null);
INSERT INTO `animation` VALUES ('2564924d-48f7-4705-8d0a-2bdaaf1090a6', 'cccc', null, null, null, null, null, '64d100e9-f521-4766-ac21-9c663d41b995', null, null);
INSERT INTO `animation` VALUES ('25683ad4-b06a-410f-8a8f-cd8a7e148794', '稻荷恋之歌/狐妖恋爱入门', 'https://www.agedm.org/?ref=github', null, '已完结', null, null, '001', '2024-10-16 15:08:21', null);
INSERT INTO `animation` VALUES ('c62d3c22-ade8-45c6-ad43-9def4ae3afc0', 'ccffc', null, null, 'end', null, null, '001', '2024-10-17 17:53:40', null);

-- ----------------------------
-- Table structure for anmation_pictures
-- ----------------------------
DROP TABLE IF EXISTS `anmation_pictures`;
CREATE TABLE `anmation_pictures` (
  `id` varchar(64) NOT NULL,
  `ssanimationid` varchar(64) DEFAULT NULL,
  `pictureUrl` varchar(255) DEFAULT NULL,
  `pictureName` varchar(255) DEFAULT NULL,
  `pictureLogic` varchar(255) DEFAULT NULL,
  `picturePath` varchar(255) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of anmation_pictures
-- ----------------------------

-- ----------------------------
-- Table structure for comic
-- ----------------------------
DROP TABLE IF EXISTS `comic`;
CREATE TABLE `comic` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `hasend` varchar(255) DEFAULT NULL,
  `pictureURL` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `sscollector` varchar(64) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  `hasendLabel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of comic
-- ----------------------------
INSERT INTO `comic` VALUES ('3095a982-e233-4458-99db-bf8884416781', 'tetyy', null, null, null, null, null, '001', '2024-10-18 10:30:13', null);

-- ----------------------------
-- Table structure for comic_pictures
-- ----------------------------
DROP TABLE IF EXISTS `comic_pictures`;
CREATE TABLE `comic_pictures` (
  `id` varchar(64) NOT NULL,
  `sscomicid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `pictureUrl` varchar(255) DEFAULT NULL,
  `pictureName` varchar(255) DEFAULT NULL,
  `pictureLogic` varchar(255) DEFAULT NULL,
  `picturePath` varchar(255) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of comic_pictures
-- ----------------------------
INSERT INTO `comic_pictures` VALUES ('0c8be7e5-8954-4528-a95c-1a5564afb858', '3095a982-e233-4458-99db-bf8884416781', null, '231skin4.jpg', '0c8be7e5-8954-4528-a95c-1a5564afb858.jpg', 'D:\\picture\\comic', '2024-10-18 10:30:13');

-- ----------------------------
-- Table structure for game
-- ----------------------------
DROP TABLE IF EXISTS `game`;
CREATE TABLE `game` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `hasend` varchar(255) DEFAULT NULL,
  `pictureURL` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `sscollector` varchar(64) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  `hasendLabel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of game
-- ----------------------------
INSERT INTO `game` VALUES ('5d83b583-25c6-4247-bdb0-86dbd2dd56de', 'ccc', null, null, null, null, null, '001', '2024-10-18 14:39:55', null);

-- ----------------------------
-- Table structure for game_pictures
-- ----------------------------
DROP TABLE IF EXISTS `game_pictures`;
CREATE TABLE `game_pictures` (
  `id` varchar(64) NOT NULL,
  `ssgameid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `pictureUrl` varchar(255) DEFAULT NULL,
  `pictureName` varchar(255) DEFAULT NULL,
  `pictureLogic` varchar(255) DEFAULT NULL,
  `picturePath` varchar(255) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of game_pictures
-- ----------------------------

-- ----------------------------
-- Table structure for novel
-- ----------------------------
DROP TABLE IF EXISTS `novel`;
CREATE TABLE `novel` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `hasend` varchar(255) DEFAULT NULL,
  `pictureURL` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `sscollector` varchar(64) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  `hasendLabel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of novel
-- ----------------------------

-- ----------------------------
-- Table structure for novel_pictures
-- ----------------------------
DROP TABLE IF EXISTS `novel_pictures`;
CREATE TABLE `novel_pictures` (
  `id` varchar(64) NOT NULL,
  `ssnovelid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `pictureUrl` varchar(255) DEFAULT NULL,
  `pictureName` varchar(255) DEFAULT NULL,
  `pictureLogic` varchar(255) DEFAULT NULL,
  `picturePath` varchar(255) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of novel_pictures
-- ----------------------------
