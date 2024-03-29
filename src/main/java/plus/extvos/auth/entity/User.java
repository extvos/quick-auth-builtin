package plus.extvos.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.util.Map;

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
    @TableField(fill = FieldFill.INSERT)
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp created;

    /**
     * updated / 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
     * cellphone / 手机号码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private String cellphone;

    /**
     * email / 邮箱
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private String email;

    @TableField(exist = false)
    private Boolean isSelf;

    public Boolean getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(Boolean isSelf) {
        this.isSelf = isSelf;
    }

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String,UserOpenAccount> openAccounts;

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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, UserOpenAccount> getOpenAccounts() {
        return openAccounts;
    }

    public void setOpenAccounts(Map<String, UserOpenAccount> openAccounts) {
        this.openAccounts = openAccounts;
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
