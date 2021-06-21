package plus.extvos.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * @author Mingcai SHEN
 */
@TableName("builtin_user_permissions")
public class UserPermission {
    /**
     * userId / UserId
     */
    @TableField(value="user_id")
    private Long userId;


    /**
     * permissionId / PermissionId
     */
    @TableField(value="permission_id")
    private Integer permissionId;

    /**
     * updated / 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp updated;

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

    public UserPermission(){

    }
}
