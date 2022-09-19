package plus.extvos.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

/**
 * @author Mingcai SHEN
 */
@TableName("builtin_user_roles")
public class UserRole {
    /**
     * userId / UserId
     */
    @TableField(value = "user_id")
    private Long userId;


    /**
     * roleId / RoleId
     */
    @TableField(value = "role_id")
    private Integer roleId;

    /**
     * updated / 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp updated;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserRole(Long userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRole() {

    }
}
