/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50154
Source Host           : localhost:3306
Source Database       : gridoc

Target Server Type    : MYSQL
Target Server Version : 50154
File Encoding         : 65001

Date: 2016-05-18 18:16:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gridoc
-- ----------------------------
DROP TABLE IF EXISTS `gridoc`;
CREATE TABLE `gridoc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for gridoc_child
-- ----------------------------
DROP TABLE IF EXISTS `gridoc_child`;
CREATE TABLE `gridoc_child` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gridoc` int(11) NOT NULL,
  `section` int(11) DEFAULT NULL,
  `paragraph` int(11) DEFAULT NULL,
  `flag` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `sequence` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order` (`sequence`),
  KEY `gridoc` (`gridoc`),
  KEY `section` (`section`),
  KEY `paragraph` (`paragraph`),
  CONSTRAINT `gridoc_child_ibfk_1` FOREIGN KEY (`gridoc`) REFERENCES `gridoc` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `gridoc_child_ibfk_2` FOREIGN KEY (`section`) REFERENCES `section` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `gridoc_child_ibfk_3` FOREIGN KEY (`paragraph`) REFERENCES `paragraph` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for gridoc_user
-- ----------------------------
DROP TABLE IF EXISTS `gridoc_user`;
CREATE TABLE `gridoc_user` (
  `gridoc` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  `read_only` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`gridoc`,`user`),
  KEY `user` (`user`),
  CONSTRAINT `gridoc_user_ibfk_1` FOREIGN KEY (`gridoc`) REFERENCES `gridoc` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `gridoc_user_ibfk_2` FOREIGN KEY (`user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for paragraph
-- ----------------------------
DROP TABLE IF EXISTS `paragraph`;
CREATE TABLE `paragraph` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `keywords` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `description` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `sync_time_type` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `sync_direction_type` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `warm_sync_detail` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `data_source_type` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `data_source_path` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cache` varchar(255) COLLATE utf8_bin DEFAULT '',
  `last_sync_succeed` varchar(255) COLLATE utf8_bin DEFAULT 'N',
  `last_sync_time` datetime DEFAULT '2016-05-08 20:00:00',
  `datasource_changed` varchar(255) COLLATE utf8_bin DEFAULT 'N',
  `cache_changed` varchar(255) COLLATE utf8_bin DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for section
-- ----------------------------
DROP TABLE IF EXISTS `section`;
CREATE TABLE `section` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for section_child
-- ----------------------------
DROP TABLE IF EXISTS `section_child`;
CREATE TABLE `section_child` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `section` int(11) NOT NULL,
  `sub_section` int(11) DEFAULT NULL,
  `paragraph` int(11) DEFAULT NULL,
  `flag` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `sequence` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order` (`sequence`),
  KEY `gridoc` (`section`),
  KEY `section` (`sub_section`),
  KEY `paragraph` (`paragraph`),
  CONSTRAINT `section_child_ibfk_2` FOREIGN KEY (`section`) REFERENCES `section` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `section_child_ibfk_3` FOREIGN KEY (`paragraph`) REFERENCES `paragraph` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `section_child_ibfk_4` FOREIGN KEY (`sub_section`) REFERENCES `section` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `password` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `nickname` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
