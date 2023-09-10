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

 Date: 19/05/2021 14:06:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for game
-- ----------------------------
DROP TABLE IF EXISTS `game`;
CREATE TABLE `game`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `next` int(11) DEFAULT NULL,
  `winner` int(11) DEFAULT NULL,
  `tableId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
