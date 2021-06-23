package plus.extvos.auth.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

/**
 * 角色数据表
 *
 * @author Mingcai SHEN
 */
@TableName("builtin_roles")
public class Role {

    /**
     * id / ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * code / 角色代码
     */
    private String code;

    /**
     * name / 角色名称
     */
    private String name;

    /**
     * comment / 角色描述
     */
    private String comment;

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
    private Integer[] permissionIds;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private Permission[] permissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Role(Integer id, String code, String name, String comment, Timestamp created, Timestamp updated) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.comment = comment;
        this.created = created;
        this.updated = updated;
    }

    public Role(){

    }
}
