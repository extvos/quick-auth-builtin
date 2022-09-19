package plus.extvos.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

/**
 * @author Mingcai SHEN
 */
@TableName("builtin_user_permissions")
public class UserPermission {
    /**
     * userId / UserId
     */
    @TableField(value = "user_id")
    private Long userId;


    /**
     * permissionId / PermissionId
     */
    @TableField(value = "permission_id")
    private Integer permissionId;

    /**
     * updated / 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp updated;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Permission permission;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public UserPermission(Long userId, Integer permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }

    public UserPermission() {

    }
}
