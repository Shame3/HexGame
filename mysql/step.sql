/*
 Navicat Premium Data Transfer

 Source Server         : c4
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost:3306
 Source Schema         : hex

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 19/05/2021 14:06:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for step
-- ----------------------------
DROP TABLE IF EXISTS `step`;
CREATE TABLE `step`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gameId` int(11) DEFAULT NULL,
  `indexNum` int(11) DEFAULT NULL,
  `color` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 393 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
