/*
Navicat MySQL Data Transfer

Source Server Version : 50621
Source Database       : quantitative_analysis

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `uname` varchar(255) NOT NULL COMMENT '用户名',
  `upwd` varchar(255) DEFAULT NULL COMMENT '密码',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) unsigned zerofill DEFAULT '0000000000' COMMENT '状态，0有效，1无效',
  `role_id` int(10) unsigned zerofill DEFAULT '0000000000' COMMENT '角色id，0代表用户目前没有角色',
  `memo` text COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uname` (`uname`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_strategy
-- ----------------------------
DROP TABLE IF EXISTS `user_strategy`;
CREATE TABLE `user_strategy` (
  `id` int(12) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uname` varchar(255) NOT NULL COMMENT '用户名称',
  `strategy_name` varchar(25) DEFAULT NULL COMMENT '策略名称',
  `strategy_text` text,
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `strategy_result` longtext COMMENT '策略的回测结果',
  `strategy_detail_result` longtext COMMENT '策略的回测详情指标结果',
  `strategy_status` int(11) DEFAULT '0' COMMENT '策略的状态',
  `image_id` varchar(255) DEFAULT NULL COMMENT 'docker的镜像id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=279 DEFAULT CHARSET=utf8;
