# quick-auth-builtin

提供一套默认的本地用户认证数据存取支撑，即实现了`quick-auth-base`以及`quick-auth-oauth2`所需的基本接口，实现了用户认证的开箱即用。

模块在加载的时候自动创建数据库。

## 注意事项
###    1. pom.xml必须引入 mybatis-plus-boot-starter，单独引入mybatis 或者 mybatis-plus 启动不报错，但是调用到
###         quick-auth-builtin包内的 mapper 时，报invalid bound statement 
###    2. 然后在 yml文件中增加 mybatis-plus.mapper-locations 的配置
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.3</version>
</dependency>
```
```yaml
mybatis-plus:
  # 映射文件所在路径
  mapper-locations: classpath*:mapper/**/*.xml
  # 项目 pojo类所在包路径
  type-aliases-package: plus.extvos.auth.entity #
```

## 数据表

### `User` 用户表

```sql
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE utf8_unicode_ci COMMENT='用户数据表';
```

### `UserCellphone` 用户手机号表

```sql
CREATE TABLE IF NOT EXISTS `builtin_user_cellphones` (
	`id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `cellphone` varchar(32) NOT NULL COMMENT '手机号码',
	`created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `cellphone` (`cellphone`) USING BTREE,
	CONSTRAINT `builtin_user_cellphones_ibfk_1` FOREIGN KEY (`id`) REFERENCES `builtin_users` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户手机关联账号';
```


### `UserWechatAccount` 用户微信账号表

```sql
CREATE TABLE IF NOT EXISTS `builtin_user_wechat_accounts` (
	`id` BIGINT(20) NOT NULL COMMENT '用户ID',
	`open_id` VARCHAR(64) NOT NULL COMMENT '微信OpenID' COLLATE 'utf8mb4_general_ci',
	`nickname` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '微信昵称' COLLATE 'utf8mb4_general_ci',
	`language` VARCHAR(16) NOT NULL DEFAULT '' COMMENT '用户语言' COLLATE 'utf8mb4_general_ci',
	`city` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '用户所在城市' COLLATE 'utf8mb4_general_ci',
	`province` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '用户所在省' COLLATE 'utf8mb4_general_ci',
	`country` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '用户所在国家' COLLATE 'utf8mb4_general_ci',
	`avatar_url` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '头像URL' COLLATE 'utf8mb4_general_ci',
	`created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `open_id` (`open_id`) USING BTREE,
	CONSTRAINT `builtin_user_wechat_accounts_ibfk_1` FOREIGN KEY (`id`) REFERENCES `builtin_users` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8_unicode_ci COMMENT='用户微信关联账号';
```



### `Role` 角色表

```SQL
CREATE TABLE IF NOT EXISTS `builtin_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(32) NOT NULL COMMENT '角色代码',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `comment` varchar(128) NOT NULL COMMENT '角色描述',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8_unicode_ci COMMENT='角色数据表';
```



### `Permission` 权限表

```SQL
CREATE TABLE IF NOT EXISTS `builtin_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(32) NOT NULL COMMENT '权限代码',
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `comment` varchar(128) NOT NULL COMMENT '权限描述',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8_unicode_ci COMMENT='权限数据表';
```



### 用户与角色关联关系

```sql
CREATE TABLE IF NOT EXISTS `builtin_user_roles` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `builtin_user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `builtin_users` (`id`),
  CONSTRAINT `builtin_user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `builtin_roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8_unicode_ci COMMENT='用户角色关联表';
```



### 用户与权限关联关系

```sql
CREATE TABLE IF NOT EXISTS `builtin_user_permissions` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `permission_id` int(11) NOT NULL COMMENT '权限ID',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `builtin_user_permissions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `builtin_users` (`id`),
  CONSTRAINT `builtin_user_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `builtin_permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8_unicode_ci COMMENT='用户权限关联表';
```



### 角色与权限关联关系

```sql
CREATE TABLE IF NOT EXISTS `builtin_role_permissions` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `permission_id` int(11) NOT NULL COMMENT '权限ID',
  `updated` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `builtin_role_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `builtin_roles` (`id`),
  CONSTRAINT `builtin_role_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `builtin_permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8_unicode_ci COMMENT='角色权限关联表';

```



## 实现的接口

### 用户管理
- `GET` `/builtin/auth/user`
- `GET` `/builtin/auth/user/{id}`
- `POST` `/builtin/auth/user`
- `PUT` `/builtin/auth/user/{id}`
- `PUT` `/builtin/auth/user`
- `DELETE` `/builtin/auth/user`
- `DELETE` `/builtin/auth/user/{id}`

### 角色管理
- `GET` `/builtin/auth/role`
- `GET` `/builtin/auth/role/{id}`
- `POST` `/builtin/auth/role`
- `PUT` `/builtin/auth/role/{id}`
- `PUT` `/builtin/auth/role`
- `DELETE` `/builtin/auth/role`
- `DELETE` `/builtin/auth/role/{id}`

### 权限管理
- `GET` `/builtin/auth/permission`
- `GET` `/builtin/auth/permission/{id}`
- `POST` `/builtin/auth/permission`
- `PUT` `/builtin/auth/permission/{id}`
- `PUT` `/builtin/auth/permission`
- `DELETE` `/builtin/auth/permission`
- `DELETE` `/builtin/auth/permission/{id}`

