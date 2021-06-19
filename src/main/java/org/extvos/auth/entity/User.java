package org.extvos.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

/**
 * 用户数据表
 *
 * @author Mingcai SHEN
 */
@TableName("builtin_users")
public class User {

    /**
     * id / ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * username / 用户名
     */
    private String username;

    /**
     * password / 密码
     */
    private String password;

    /**
     * nickname / 昵称
     */
    private String nickname;

    /**
     * status / 状态: 0 = 注册, 1: 激活, -1: 锁定
     */
    private Short status;

    /**
     * created / 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp created;

    /**
     * updated / 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp updated;


    /**
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private Integer[] roleIds;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private Role[] roles;

    /**
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private Integer[] permissionIds;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private Permission[] permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Integer[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Integer[] roleIds) {
        this.roleIds = roleIds;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    public Integer[] getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(Integer[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    public Permission[] getPermissions() {
        return permissions;
    }

    public void setPermissions(Permission[] permissions) {
        this.permissions = permissions;
    }

    public User(Long id, String username, String password, String nickname, Short status, Timestamp created, Timestamp updated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public User() {

    }
}
