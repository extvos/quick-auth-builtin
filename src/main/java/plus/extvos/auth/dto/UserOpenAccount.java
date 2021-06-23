package plus.extvos.auth.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.Map;

/**
 * 用户第三方开放账户关联账号
 * @author Mingcai SHEN
 */
@TableName("builtin_user_open_accounts")
public class UserOpenAccount {
    /**
     * id / ID
     */
    @TableField(value="id")
    private Long id;

    /**
     * provider / 开发账号提供方,与OAuth2 Provider对应
     */
    @TableField(value="provider")
    private String provider;


    /**
     * userId / 用户ID
     */
    @TableField(value="user_id")
    private Long userId;

    /**
     * open_id / OpenID
     */
    @TableField(value="open_id")
    private String openId;

    /**
     * nickname / 昵称
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
     * extras / 扩展信息
     */
    @TableField(value="extras")
    @JsonIgnore
    private String extraString;

    @TableField(exist = false)
    private Map<String,Object> extras;

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getExtraString() {
        return extraString;
    }

    public void setExtraString(String extraString) {
        this.extraString = extraString;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
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
}
