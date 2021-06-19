package org.extvos.auth.entity;

/**
 * @author shenmc
 */

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 用户微信关联账号
 * @author Mingcai SHEN
 */
@TableName("builtin_user_cellphones")
public class UserCellphone {
    /**
     * id / 用户ID
     */
    @TableField(value="id")
    private Long id;

    /**
     * cellphone / 手机号码
     */
    private String cellphone;

    /**
     * created / 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @TableField(value="created")
    private Timestamp created;

    /**
     * updated / 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @TableField(value="updated")
    private Timestamp updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
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

    public UserCellphone(){
    }

    public UserCellphone(Long id, String cellphone) {
        this.id = id;
        this.cellphone = cellphone;
    }
}
