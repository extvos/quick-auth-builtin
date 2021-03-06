-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.32 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 导出  表 builtin_permissions 结构
CREATE TABLE IF NOT EXISTS `builtin_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(32) NOT NULL COMMENT '权限代码',
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `comment` varchar(128) NOT NULL COMMENT '权限描述',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3001 DEFAULT CHARSET=utf8mb4 COMMENT='权限数据表';

-- 数据导出被取消选择。

-- 导出  表 builtin_roles 结构
CREATE TABLE IF NOT EXISTS `builtin_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(32) NOT NULL COMMENT '角色代码',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `comment` varchar(128) NOT NULL COMMENT '角色描述',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2001 DEFAULT CHARSET=utf8mb4 COMMENT='角色数据表';

-- 数据导出被取消选择。

-- 导出  表 builtin_role_permissions 结构
CREATE TABLE IF NOT EXISTS `builtin_role_permissions` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `permission_id` int(11) NOT NULL COMMENT '权限ID',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `builtin_role_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `builtin_roles` (`id`),
  CONSTRAINT `builtin_role_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `builtin_permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 数据导出被取消选择。

-- 导出  表 builtin_users 结构
CREATE TABLE IF NOT EXISTS `builtin_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `nickname` varchar(64) NOT NULL COMMENT '昵称',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '状态: 0 = 注册, 1: 激活, -1: 锁定',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='用户数据表';

-- 数据导出被取消选择。

-- 导出  表 builtin_user_permissions 结构
CREATE TABLE IF NOT EXISTS `builtin_user_permissions` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `permission_id` int(11) NOT NULL COMMENT '权限ID',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `builtin_user_permissions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `builtin_users` (`id`),
  CONSTRAINT `builtin_user_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `builtin_permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户权限关联表';

-- 数据导出被取消选择。

-- 导出  表 builtin_user_roles 结构
CREATE TABLE IF NOT EXISTS `builtin_user_roles` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `builtin_user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `builtin_users` (`id`),
  CONSTRAINT `builtin_user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `builtin_roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 数据导出被取消选择。

-- 导出  表 builtin_user_open_accounts 结构
CREATE TABLE IF NOT EXISTS `builtin_user_open_accounts` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`provider` VARCHAR(64) NOT NULL COMMENT 'Provider' COLLATE 'utf8mb4_general_ci',
	`user_id` bigint(20) NOT NULL COMMENT '用户ID',
	`open_id` VARCHAR(64) NOT NULL COMMENT 'OpenID' COLLATE 'utf8mb4_general_ci',
	`nickname` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '昵称' COLLATE 'utf8mb4_general_ci',
	`language` VARCHAR(16) NOT NULL DEFAULT '' COMMENT '用户语言' COLLATE 'utf8mb4_general_ci',
	`city` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '用户所在城市' COLLATE 'utf8mb4_general_ci',
	`province` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '用户所在省' COLLATE 'utf8mb4_general_ci',
	`country` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '用户所在国家' COLLATE 'utf8mb4_general_ci',
	`avatar_url` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '头像URL' COLLATE 'utf8mb4_general_ci',
	`extras` TEXT NULL COMMENT '扩展信息' COLLATE 'utf8mb4_general_ci',
	`created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `open_user` (`provider`, `user_id`) USING BTREE,
	UNIQUE INDEX `open_account` (`provider`, `open_id`) USING BTREE,
	CONSTRAINT `builtin_user_open_accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `builtin_users` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户第三方开放账户关联账号';
-- 数据导出被取消选择。

-- 导出  表 builtin_user_cellphones 结构
CREATE TABLE IF NOT EXISTS `builtin_user_cellphones` (
	`id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `cellphone` varchar(32) NOT NULL COMMENT '手机号码',
	`created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `cellphone` (`cellphone`) USING BTREE,
	CONSTRAINT `builtin_user_cellphones_ibfk_1` FOREIGN KEY (`id`) REFERENCES `builtin_users` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户手机关联账号';
-- 数据导出被取消选择。

-- 导出  表 builtin_user_emails 结构
CREATE TABLE IF NOT EXISTS `builtin_user_emails` (
	`id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `email` varchar(128) NOT NULL COMMENT '邮件地址',
	`created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `email` (`email`) USING BTREE,
	CONSTRAINT `builtin_user_emails_ibfk_1` FOREIGN KEY (`id`) REFERENCES `builtin_users` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户邮件关联账号';
-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
