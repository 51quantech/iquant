/*
Navicat MySQL Data Transfer

Source Server         : 10.13.85.181
Source Server Version : 50621
Source Host           : 10.13.85.181:3306
Source Database       : quantitative_analysis

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2016-08-29 20:26:05
*/

-- ----------------------------
-- Table structure for resources
-- ----------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `url` text ,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `status` int(10) unsigned zerofill DEFAULT '0000000000',
  `memo` varchar(255) DEFAULT NULL ,
  `pid` int(11) unsigned zerofill DEFAULT '00000000000',
  `view_name` varchar(255) DEFAULT NULL ,
  `view_order` int(11) DEFAULT '0' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) unsigned zerofill DEFAULT '0000000000' COMMENT '状态，0有效,1无效',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_resources
-- ----------------------------
DROP TABLE IF EXISTS `role_resources`;
CREATE TABLE `role_resources` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色资源关联ID',
  `role_id` int(10) unsigned zerofill NOT NULL DEFAULT '0000000000' COMMENT '关联的角色id',
  `resources_id` int(10) unsigned zerofill NOT NULL DEFAULT '0000000000' COMMENT '关联的资源id',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) unsigned zerofill DEFAULT '0000000000' COMMENT '状态，0有效,1无效',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `uname` varchar(255) NOT NULL COMMENT '用户名',
  `upwd` varchar(255) DEFAULT NULL COMMENT '密码，目前使用SSO登录，密码都为空',
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

-- ----------------------------
-- Table structure for user_strategy_top
-- ----------------------------
DROP TABLE IF EXISTS `user_strategy_top`;
CREATE TABLE `user_strategy_top` (
  `id` int(12) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uname` varchar(255) NOT NULL COMMENT '用户名称',
  `strategy_name` varchar(25) DEFAULT NULL COMMENT '策略名称',
  `strategy_text` text,
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `strategy_result` longtext COMMENT '策略的回测结果',
  `strategy_status` int(11) DEFAULT NULL COMMENT '策略的状态',
  `alpha` float DEFAULT NULL COMMENT '阿尔法',
  `benchmark_return_rate` float DEFAULT NULL COMMENT '基准年化收益率',
  `beta` float DEFAULT NULL COMMENT '贝塔',
  `information_ratio` float DEFAULT NULL COMMENT '信息比率',
  `max_drawdown` float DEFAULT NULL COMMENT '最大回撤',
  `sharpe_ratio` float DEFAULT NULL COMMENT '夏普率比',
  `strategy_return_rate` float DEFAULT NULL COMMENT '年化收益率',
  `turnover_rate` float DEFAULT NULL COMMENT '换手率',
  `volatility` float DEFAULT NULL COMMENT '收益波动率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
