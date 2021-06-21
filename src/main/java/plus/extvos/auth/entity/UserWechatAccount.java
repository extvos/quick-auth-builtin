package plus.extvos.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 用户微信关联账号
 * @author Mingcai SHEN
 */
@TableName("builtin_user_wechat_accounts")
public class UserWechatAccount {

    /**
     * id / 用户ID
     */
    @TableField(value="id")
    private Long id;

    /**
     * open_id / 微信OpenID
     */
    @TableField(value="open_id")
    private String openId;

    /**
     * nickname / 微信昵称
     */
    @TableField(value="nickname")
    private String nickname;

    /**
     * language / 用户语言
     */
    @TableField(value="language")
    private String language;

    /**
     * city / 用户所在城市
     */
    @TableField(value="city")
    private String city;

    /**
     * province / 用户所在省
     */
    @TableField(value="province")
    private String province;

    /**
     * country / 用户所在国家
     */
    @TableField(value="country")
    private String country;

    /**
     * avatar_url / 头像URL
     */
    @TableField(value="avatar_url")
    private String avatarUrl;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public UserWechatAccount(Long id, String openId, String nickname, String language, String city, String province, String country, String avatarUrl, Timestamp created, Timestamp updated) {
        this.id = id;
        this.openId = openId;
        this.nickname = nickname;
        this.language = language;
        this.city = city;
        this.province = province;
        this.country = country;
        this.avatarUrl = avatarUrl;
        this.created = created;
        this.updated = updated;
    }

    public UserWechatAccount(){

    }

}
