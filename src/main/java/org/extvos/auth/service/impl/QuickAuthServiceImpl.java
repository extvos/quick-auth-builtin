package org.extvos.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.extvos.auth.dto.PermissionInfo;
import org.extvos.auth.dto.RoleInfo;
import org.extvos.auth.dto.UserInfo;
import org.extvos.auth.entity.UserWechatAccount;
import org.extvos.auth.enums.AuthCode;
import org.extvos.auth.mapper.UserWechatAccountMapper;
import org.extvos.auth.service.*;
import org.extvos.auth.entity.Permission;
import org.extvos.auth.entity.Role;
import org.extvos.auth.entity.User;
import org.extvos.auth.mapper.PermissionMapper;
import org.extvos.auth.mapper.RoleMapper;
import org.extvos.auth.mapper.UserMapper;
import org.extvos.auth.service.wechat.WechatOAuthServiceProvider;
import org.extvos.auth.utils.CredentialHash;
import org.extvos.restlet.Assert;
import org.extvos.restlet.exception.RestletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Mingcai SHEN
 */
@Service
public class QuickAuthServiceImpl implements QuickAuthService, OpenIdResolver {

    private static final Logger log = LoggerFactory.getLogger(QuickAuthServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserWechatAccountMapper userWechatAccountMapper;


    @Override
    public UserInfo getUserByName(String name, boolean checkEnabled) throws RestletException {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", name);
        User u = userMapper.selectOne(qw);
        if (null == u) {
            throw new RestletException(AuthCode.ACCOUNT_NOT_FOUND, "user not found");
        } else if (checkEnabled && u.getStatus() < 1) {
            if (u.getStatus() == 0) {
                throw new RestletException(AuthCode.ACCOUNT_DISABLED, "user not activated");
            } else {
                throw new RestletException(AuthCode.ACCOUNT_LOCKED, "user locked");
            }
        } else {
            return new UserInfo(u.getId(), u.getUsername(), u.getPassword());
        }
    }

    @Override
    public UserInfo getUserById(Serializable id, boolean checkEnabled) throws RestletException {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", id);
        User u = userMapper.selectOne(qw);
        if (null == u) {
            return null;
        } else if (checkEnabled && u.getStatus() < 1) {
            if (u.getStatus() == 0) {
                throw RestletException.forbidden("user not activated");
            } else {
                throw RestletException.forbidden("user was locked");
            }
        } else {
            return new UserInfo(u.getId(), u.getUsername(), u.getPassword());
        }
    }

    @Override
    public List<RoleInfo> getRoles(Serializable id) throws RestletException {
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.select("code", "name");
        qw.inSql("id", "SELECT role_id FROM builtin_user_roles WHERE user_id =" + id);
        List<Role> ls = roleMapper.selectList(qw);
        List<RoleInfo> rs = new LinkedList<>();
        ls.forEach(role -> {
            rs.add(new RoleInfo(role.getCode(), role.getName()));
        });
        return rs;
    }

    @Override
    public List<PermissionInfo> getPermissions(Serializable id) throws RestletException {
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.select("code", "name");
        qw.inSql("id", "SELECT permission_id FROM builtin_user_permissions WHERE user_id =" + id);
        qw.or().inSql("id", "SELECT permission_id FROM builtin_role_permissions WHERE role_id IN (SELECT role_id FROM builtin_user_roles WHERE user_id =" + id + ")");
        List<Permission> ls = permissionMapper.selectList(qw);
        List<PermissionInfo> rs = new LinkedList<>();
        ls.forEach(perm -> {
            rs.add(new PermissionInfo(perm.getCode(), perm.getName()));
        });
        return rs;
    }

    @Override
    public Serializable createUserInfo(String username, String password, String[] permissions, String[] roles, Map<String, Object> params) throws RestletException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if (params != null) {
            user.setCellphone(params.getOrDefault("cellphone", "").toString());
            user.setNickname(params.getOrDefault("nickname", "username").toString());
            user.setStatus(Short.parseShort(params.getOrDefault("status", "0").toString()));
        }
        return userMapper.insert(user);
    }

    @Override
    public void updateUserInfo(String username, String password, String[] permissions, String[] roles, Map<String, Object> params) throws RestletException {
        User user = new User();
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        user.setUpdated(new Timestamp(System.currentTimeMillis()));
        if (params != null) {
            user.setCellphone(params.getOrDefault("cellphone", "").toString());
            user.setNickname(params.getOrDefault("nickname", "username").toString());
            user.setStatus(Short.parseShort(params.getOrDefault("status", "0").toString()));
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        userMapper.update(user, qw);
    }

    @Override
    public UserInfo resolve(String provider, String openId, Serializable userId, Map<String, Object> params) throws RestletException {
        log.debug("resolve:>>> {}, {} ", provider, openId);
        Assert.equals(provider, WechatOAuthServiceProvider.SLUG, RestletException.badRequest("unknown provider: " + provider));
        QueryWrapper<UserWechatAccount> qw = new QueryWrapper<>();
        if (userId != null) {
            qw.eq("id", userId);
        } else {
            qw.eq("open_id", openId);
        }
        UserWechatAccount uwa = userWechatAccountMapper.selectOne(qw);
        if (null == uwa) {
            return null;
        } else if (!uwa.getOpenId().equals(openId)) {
            throw RestletException.conflict("user '" + userId + "' already linked with '" + uwa.getOpenId() + "'");
        }
        User user = userMapper.selectById(uwa.getId());
        if (null == user) {
            return null;
        }
        return new UserInfo(user.getId(), user.getUsername(), user.getPassword());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo register(String provider, String openId, String username, Map<String, Object> params) throws RestletException {
        Assert.equals(provider, WechatOAuthServiceProvider.SLUG, RestletException.badRequest("unknown provider: " + provider));
        QueryWrapper<User> qw = new QueryWrapper<>();
        Assert.notEmpty(openId, RestletException.badRequest("openId required"));
        QueryWrapper<UserWechatAccount> qwu = new QueryWrapper<>();
        qwu.eq("open_id", openId);
        if (userWechatAccountMapper.selectCount(qwu) > 0) {
            throw RestletException.conflict("open_id '" + openId + "' already exists");
        }

        User user = null;
        if (username == null || username.isEmpty()) {
            username = openId;
        }
        qw.eq("username", username);
        user = userMapper.selectOne(qw);
        if (user == null) {
            log.info("User {} not exists, register now ...", username);
            user = new User();
            user.setUsername(username);
            try {
                user.setPassword(CredentialHash.password(openId).encrypt());
            } catch (Exception e) {
                user.setPassword(CredentialHash.generateSalt(24));
            }
            if (params != null) {
                user.setNickname(params.getOrDefault("nickname", openId).toString());
                user.setCellphone(params.getOrDefault("cellphone", openId).toString());
            }
            user.setStatus((short) 1);
            userMapper.insert(user);
            user = userMapper.selectOne(qw);
            if (null == user) {
                throw RestletException.internalServerError("register user failed");
            }
            log.info("Created user {}, linking with {} now ...", username, openId);
        } else {
            log.info("User {} exists, linking with {} now ...", username, openId);
        }
        UserWechatAccount uwa = new UserWechatAccount();
        uwa.setId(user.getId());
        uwa.setOpenId(openId);
        if (params != null) {
            uwa.setNickname(params.getOrDefault("nickname", openId).toString());
            uwa.setAvatarUrl(params.getOrDefault("headimgurl", "").toString());
            uwa.setCity(params.getOrDefault("city", "SZ").toString());
            uwa.setProvince(params.getOrDefault("province", "GD").toString());
            uwa.setCountry(params.getOrDefault("country", "CN").toString());
            uwa.setLanguage(params.getOrDefault("language", "zh-CN").toString());
        }
        userWechatAccountMapper.insert(uwa);
        return new UserInfo(user.getId(), user.getUsername(), user.getPassword());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(String provider, String openId, Map<String, Object> params) throws RestletException {
        Assert.equals(provider, WechatOAuthServiceProvider.SLUG, RestletException.badRequest("unknown provider: " + provider));
        Assert.notEmpty(openId, RestletException.badRequest("openId required"));
        Assert.notEmpty(params, RestletException.badRequest("params can not be empty"));
        log.debug("update:> {}, {}", openId, params);
        QueryWrapper<UserWechatAccount> qwu = new QueryWrapper<>();
        qwu.eq("open_id", openId);
        if (userWechatAccountMapper.selectCount(qwu) <= 0) {
            return false;
        }
        boolean updated = false;
        UserWechatAccount uwa = new UserWechatAccount();
        if (params.containsKey("nickname")) {
            uwa.setNickname(params.get("nickname").toString());
            updated = true;
        }
        if (params.containsKey("headimgurl")) {
            uwa.setAvatarUrl(params.get("headimgurl").toString());
            updated = true;
        }
        if (params.containsKey("language")) {
            uwa.setLanguage(params.get("language").toString());
            updated = true;
        }
        if (params.containsKey("country")) {
            uwa.setCountry(params.get("country").toString());
            updated = true;
        }
        if (params.containsKey("province")) {
            uwa.setProvince(params.get("province").toString());
            updated = true;
        }
        if (params.containsKey("city")) {
            uwa.setCity(params.get("city").toString());
            updated = true;
        }
        if (!updated) {
            return false;
        }
        uwa.setUpdated(new Timestamp(System.currentTimeMillis()));
        if (userWechatAccountMapper.update(uwa, qwu) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
