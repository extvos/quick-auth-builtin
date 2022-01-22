-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE SEQUENCE IF NOT EXISTS builtin_permissions_seq;

CREATE TABLE IF NOT EXISTS builtin_permissions (
  id int NOT NULL DEFAULT NEXTVAL ('builtin_permissions_seq') ,
  code varchar(32) NOT NULL ,
  name varchar(64) NOT NULL ,
  comment varchar(128) NOT NULL ,
  created timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  updated timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id),
  CONSTRAINT builtin_permissions_uq_code UNIQUE  (code)
)   ;
COMMENT ON TABLE builtin_permissions IS '内置系统权限表';
ALTER SEQUENCE builtin_permissions_seq RESTART WITH 3001;


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE SEQUENCE IF NOT EXISTS builtin_roles_seq;

CREATE TABLE IF NOT EXISTS builtin_roles (
  id int NOT NULL DEFAULT NEXTVAL ('builtin_roles_seq') ,
  code varchar(32) NOT NULL ,
  name varchar(64) NOT NULL ,
  comment varchar(128) NOT NULL ,
  created timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  updated timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id),
  CONSTRAINT builtin_roles_uq_code UNIQUE  (code)
)   ;
COMMENT ON TABLE builtin_roles IS '内置系统角色表';
ALTER SEQUENCE builtin_roles_seq RESTART WITH 2001;


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE IF NOT EXISTS builtin_role_permissions (
  role_id int NOT NULL ,
  permission_id int NOT NULL ,
  updated timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (role_id,permission_id)
 ,
  CONSTRAINT builtin_role_permissions_ibfk_1 FOREIGN KEY (role_id) REFERENCES builtin_roles (id) ON DELETE CASCADE ,
  CONSTRAINT builtin_role_permissions_ibfk_2 FOREIGN KEY (permission_id) REFERENCES builtin_permissions (id) ON DELETE CASCADE 
)  ;
COMMENT ON TABLE builtin_role_permissions IS '内置系统角色权限';
CREATE INDEX IF NOT EXISTS builtin_role_permissions_idx_permission_id ON builtin_role_permissions (permission_id);


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE SEQUENCE IF NOT EXISTS builtin_users_seq;

CREATE TABLE IF NOT EXISTS builtin_users (
  id bigint NOT NULL DEFAULT NEXTVAL ('builtin_users_seq') ,
  username varchar(32) NOT NULL ,
  password varchar(64) NOT NULL ,
  nickname varchar(64) NOT NULL ,
  status smallint NOT NULL DEFAULT '0' ,
  created timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  updated timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id),
  CONSTRAINT builtin_users_uq_username UNIQUE  (username)
)   ;
COMMENT ON TABLE builtin_users IS '内置系统用户';

ALTER SEQUENCE builtin_users_seq RESTART WITH 1001;


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE IF NOT EXISTS builtin_user_permissions (
  user_id bigint NOT NULL ,
  permission_id int NOT NULL ,
  updated timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (user_id,permission_id)
 ,
  CONSTRAINT builtin_user_permissions_ibfk_1 FOREIGN KEY (user_id) REFERENCES builtin_users (id) ON DELETE CASCADE ,
  CONSTRAINT builtin_user_permissions_ibfk_2 FOREIGN KEY (permission_id) REFERENCES builtin_permissions (id) ON DELETE CASCADE 
)  ;

COMMENT ON TABLE builtin_user_permissions IS '内置系统用户权限';
CREATE INDEX IF NOT EXISTS builtin_user_permissions_idx_permission_id ON builtin_user_permissions (permission_id);


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE IF NOT EXISTS builtin_user_roles (
  user_id bigint NOT NULL ,
  role_id int NOT NULL ,
  updated timestamp(0) DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (user_id,role_id)
 ,
  CONSTRAINT builtin_user_roles_ibfk_1 FOREIGN KEY (user_id) REFERENCES builtin_users (id)  ON DELETE CASCADE ,
  CONSTRAINT builtin_user_roles_ibfk_2 FOREIGN KEY (role_id) REFERENCES builtin_roles (id)  ON DELETE CASCADE 
)  ;
COMMENT ON TABLE builtin_user_roles IS '内置系统用户角色';
CREATE INDEX IF NOT EXISTS builtin_user_roles_idx_role_id ON builtin_user_roles (role_id);


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE SEQUENCE IF NOT EXISTS builtin_user_open_accounts_seq;

CREATE TABLE IF NOT EXISTS builtin_user_open_accounts (
	id bigint NOT NULL DEFAULT NEXTVAL ('builtin_user_open_accounts_seq') ,
	provider VARCHAR(64) NOT NULL ,
	user_id bigint NOT NULL ,
	open_id VARCHAR(64) NOT NULL ,
	nickname VARCHAR(64) NOT NULL DEFAULT '' ,
	language VARCHAR(16) NOT NULL DEFAULT '' ,
	city VARCHAR(64) NOT NULL DEFAULT '' ,
	province VARCHAR(64) NOT NULL DEFAULT '' ,
	country VARCHAR(64) NOT NULL DEFAULT '' ,
	avatar_url VARCHAR(256) NOT NULL DEFAULT '' ,
	extras TEXT NULL ,
	created TIMESTAMP(0) NULL DEFAULT CURRENT_TIMESTAMP ,
	updated TIMESTAMP(0) NULL DEFAULT CURRENT_TIMESTAMP ,
	PRIMARY KEY (id),
	CONSTRAINT builtin_user_open_accounts_uq_open_user UNIQUE  (provider, user_id),
	CONSTRAINT builtin_user_open_accounts_uq_open_account UNIQUE  (provider, open_id),
	CONSTRAINT builtin_user_open_accounts_ibfk_1 FOREIGN KEY (user_id) REFERENCES builtin_users (id)  ON DELETE CASCADE 
)   ;
COMMENT ON TABLE builtin_user_open_accounts IS '内置系统用户开放账号';

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE IF NOT EXISTS builtin_user_cellphones (
	id BIGINT NOT NULL ,
    cellphone varchar(32) NOT NULL ,
	created TIMESTAMP(0) NULL DEFAULT CURRENT_TIMESTAMP ,
	updated TIMESTAMP(0) NULL DEFAULT CURRENT_TIMESTAMP ,
	PRIMARY KEY (id),
	CONSTRAINT builtin_user_cellphones_uq_cellphone UNIQUE  (cellphone),
	CONSTRAINT builtin_user_cellphones_ibfk_1 FOREIGN KEY (id) REFERENCES builtin_users (id)  ON DELETE CASCADE 
)   ;
COMMENT ON TABLE builtin_user_cellphones IS '内置系统用户电话';
-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE IF NOT EXISTS builtin_user_emails (
	id BIGINT NOT NULL ,
    email varchar(128) NOT NULL ,
	created TIMESTAMP(0) NULL DEFAULT CURRENT_TIMESTAMP ,
	updated TIMESTAMP(0) NULL DEFAULT CURRENT_TIMESTAMP ,
	PRIMARY KEY (id),
	CONSTRAINT builtin_user_emails_uq_email UNIQUE  (email),
	CONSTRAINT builtin_user_emails_ibfk_1 FOREIGN KEY (id) REFERENCES builtin_users (id)  ON DELETE CASCADE 
)   ;
COMMENT ON TABLE builtin_user_emails IS '内置系统用户邮件';