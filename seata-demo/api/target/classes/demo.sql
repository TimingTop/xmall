create database seata_order;
create database seata_storage;
create database seata_account;

DROP TABLE IF EXISTS `t_account`;
CREATE TABLE  `t_account` (
    `id` int(11) not null auto_increment,
    `user_id` varchar(255) default null,
    `money` decimal(10,2) default '0.0',
    primary key(`id`)
) ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8;

insert into t_account (user_id, money) value('1', '4000');

DROP TABLE IF EXISTS `t_storage`;
CREATE TABLE  `t_storage` (
    `id` int(11) not null auto_increment,
    `commodity_code` varchar(255) default null,
    `count` int(11) default '0',
    primary key(`id`)
) ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8;

insert into t_storage(commodity_code, count) value('c1', '2000');

DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`(
    `id` int(11) not null auto_increment,
    `user_id` varchar(255) default null,
    `commodity_code` varchar(255) default null,
    `count` int(11) default '0',
    `money` decimal(10,2) default '0.0',
    primary key(`id`)
) ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8;


CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

