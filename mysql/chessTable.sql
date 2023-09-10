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

 Date: 19/05/2021 14:06:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chessTable
-- ----------------------------
DROP TABLE IF EXISTS `chessTable`;
CREATE TABLE `chessTable`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userRed` int(11) DEFAULT NULL,
  `userBlue` int(11) DEFAULT NULL,
  `gameId` int(11) DEFAULT NULL,
  `up` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chessTable
-- ----------------------------
INSERT INTO `chessTable` VALUES (1, 2, -1, 31, '2021-05-17 19:17:35');
INSERT INTO `chessTable` VALUES (2, -1, -1, -1, '2021-05-12 21:52:58');
INSERT INTO `chessTable` VALUES (3, -1, -1, -1, '2021-04-19 22:04:44');
INSERT INTO `chessTable` VALUES (4, -1, -1, -1, '2021-04-17 19:17:03');
INSERT INTO `chessTable` VALUES (5, -1, -1, -1, '2021-04-19 18:02:05');
INSERT INTO `chessTable` VALUES (6, -1, -1, -1, '2021-04-17 19:17:37');
INSERT INTO `chessTable` VALUES (7, -1, -1, -1, '2021-04-17 19:17:50');
INSERT INTO `chessTable` VALUES (8, -1, -1, -1, '2021-04-19 21:17:06');
INSERT INTO `chessTable` VALUES (9, -1, -1, -1, '2021-04-17 19:18:27');
INSERT INTO `chessTable` VALUES (10, -1, -1, -1, '2021-04-17 19:18:43');
INSERT INTO `chessTable` VALUES (11, -1, -1, -1, '2021-04-19 13:45:49');
INSERT INTO `chessTable` VALUES (12, -1, -1, -1, '2021-04-19 13:46:02');

SET FOREIGN_KEY_CHECKS = 1;
