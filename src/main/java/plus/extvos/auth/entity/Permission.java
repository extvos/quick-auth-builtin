package plus.extvos.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 权限数据表
 *
 * @author Mingcai SHEN
 */
@TableName("builtin_permissions")
public class Permission {

    /**
     * id / ID
     */
    @TableId(type = IdType.AUTO)
    @TableField(fill = FieldFill.INSERT)
    private Integer id;

    /**
     * code / 权限代码
     */
    private String code;

    /**
     * name / 权限名称
     */
    private String name;

    /**
     * comment / 权限描述
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

    public Permission(Integer id, String code, String name, String comment, Timestamp created, Timestamp updated) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.comment = comment;
        this.created = created;
        this.updated = updated;
    }

    public Permission() {
    }
}
