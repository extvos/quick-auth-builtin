package plus.extvos.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

/**
 * @author Mingcai SHEN
 */
@TableName("builtin_role_permissions")
public class RolePermission {
    /**
     * roleId / RoleId
     */
    @TableField(value = "role_id")
    private Integer roleId;

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
    private Role role;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Permission permission;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public RolePermission(Integer roleId, Integer permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public RolePermission() {

    }
}
