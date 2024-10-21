/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80037
Source Host           : localhost:3306
Source Database       : test_all

Target Server Type    : MYSQL
Target Server Version : 80037
File Encoding         : 65001

Date: 2024-10-21 17:28:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for url_collection
-- ----------------------------
DROP TABLE IF EXISTS `url_collection`;
CREATE TABLE `url_collection` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `urlname` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `urllogopath` varchar(255) DEFAULT NULL,
  `ssurltypeid` varchar(255) DEFAULT NULL,
  `notes` text,
  `sscollector` varchar(64) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of url_collection
-- ----------------------------
INSERT INTO `url_collection` VALUES ('001', '1111', null, null, '01', null, '001', null);

-- ----------------------------
-- Table structure for url_type_collection
-- ----------------------------
DROP TABLE IF EXISTS `url_type_collection`;
CREATE TABLE `url_type_collection` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `typename` varchar(255) DEFAULT NULL,
  `sscollector` varchar(64) DEFAULT NULL,
  `createTime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of url_type_collection
-- ----------------------------
INSERT INTO `url_type_collection` VALUES ('01', '1', '001', null);
