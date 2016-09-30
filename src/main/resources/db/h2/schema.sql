CREATE TABLE IF NOT EXISTS USER(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(255) NOT NULL,
  `upwd` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `status` int(10),
  `role_id` int(10),
  `memo` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uname` (`uname`)
);

CREATE TABLE IF NOT EXISTS USER_STRATEGY (
  `id` int(12) unsigned NOT NULL AUTO_INCREMENT,
  `uname` varchar(255) NOT NULL,
  `strategy_name` varchar(25) DEFAULT NULL,
  `strategy_text` text,
  `modified` datetime DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `strategy_result` longtext,
  `strategy_detail_result` text,
  `strategy_status` int(11),
  `image_id` varchar(255),
  PRIMARY KEY (`id`)
);
