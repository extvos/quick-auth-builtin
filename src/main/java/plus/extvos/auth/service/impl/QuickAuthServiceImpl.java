package plus.extvos.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import plus.extvos.auth.dto.OAuthInfo;
import plus.extvos.auth.dto.PermissionInfo;
import plus.extvos.auth.dto.RoleInfo;
import plus.extvos.auth.dto.UserInfo;
import plus.extvos.auth.entity.*;
import plus.extvos.auth.enums.AuthCode;
import plus.extvos.auth.mapper.*;
import plus.extvos.auth.service.*;
import plus.extvos.auth.service.wechat.WechatOAuthServiceProvider;
import plus.extvos.common.Validator;
import plus.extvos.common.utils.QuickHash;
import plus.extvos.restlet.Assert;
import plus.extvos.restlet.exception.RestletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
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
    private UserOpenAccountMapper userOpenAccountMapper;

    @Autowired
    UserCellphoneMapper userCellphoneMapper;


    @Override
    public UserInfo getUserByName(String name, boolean checkEnabled) throws RestletException {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", name);
        User u = userMapper.selectOne(qw);
        if (null == u) {
            return null;
            // throw new RestletException(AuthCode.ACCOUNT_NOT_FOUND, "user not found");
        } else if (checkEnabled && u.getStatus() < 1) {
            if (u.getStatus() == 0) {
                throw new RestletException(AuthCode.ACCOUNT_DISABLED, "user not activated");
            } else {
                throw new RestletException(AuthCode.ACCOUNT_LOCKED, "user locked");
            }
        } else {
            UserCellphone uc = userCellphoneMapper.selectById(u.getId());
            String cellphone = uc != null ? uc.getCellphone() : null;
            UserInfo ui = new UserInfo(u.getId(), u.getUsername(), u.getPassword(), cellphone);
//            QueryWrapper<UserWechatAccount> uqw = new QueryWrapper<>();
//            uqw.eq("id", u.getId());
//            UserWechatAccount uwa = userOpenAccountMapper.selectOne(uqw);
//            if (null != uwa) {
//                Map<String, Object> extraInfo = new HashMap<>();
//                extraInfo.put(OAuthProvider.NICK_NAME_KEY, uwa.getNickname());
//                extraInfo.put(OAuthProvider.AVATAR_URL_KEY, uwa.getAvatarUrl());
//                extraInfo.put(OAuthProvider.COUNTRY_KEY, uwa.getCountry());
//                extraInfo.put(OAuthProvider.PROVINCE_KEY, uwa.getProvince());
//                extraInfo.put(OAuthProvider.CITY_KEY, uwa.getCity());
//                extraInfo.put(OAuthProvider.LANGUAGE_KEY, uwa.getLanguage());
//                extraInfo.put(OAuthProvider.PHONE_NUMBER_KEY, cellphone);
//                ui.setExtraInfo(extraInfo);
//            }
            return ui;
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
            UserCellphone uc = userCellphoneMapper.selectById(u.getId());
            String cellphone = uc != null ? uc.getCellphone() : null;
            UserInfo ui = new UserInfo(u.getId(), u.getUsername(), u.getPassword(), cellphone);
//            QueryWrapper<UserWechatAccount> uqw = new QueryWrapper<>();
//            uqw.eq("id", u.getId());
//            UserWechatAccount uwa = userOpenAccountMapper.selectOne(uqw);
//            if (null != uwa) {
//                Map<String, Object> extraInfo = new HashMap<>();
//                extraInfo.put(OAuthProvider.NICK_NAME_KEY, uwa.getNickname());
//                extraInfo.put(OAuthProvider.AVATAR_URL_KEY, uwa.getAvatarUrl());
//                extraInfo.put(OAuthProvider.COUNTRY_KEY, uwa.getCountry());
//                extraInfo.put(OAuthProvider.PROVINCE_KEY, uwa.getProvince());
//                extraInfo.put(OAuthProvider.CITY_KEY, uwa.getCity());
//                extraInfo.put(OAuthProvider.LANGUAGE_KEY, uwa.getLanguage());
//                extraInfo.put(OAuthProvider.PHONE_NUMBER_KEY, cellphone);
//                ui.setExtraInfo(extraInfo);
//            }
            return ui;
        }
    }

    @Override
    public UserInfo getUserByPhone(String phone, boolean checkEnabled) throws RestletException {
        QueryWrapper<UserCellphone> qwuc = new QueryWrapper<>();
        qwuc.eq("cellphone", phone);
        UserCellphone uc = userCellphoneMapper.selectOne(qwuc);
        if (null == uc) {
            return null;
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", uc.getId());
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
            UserInfo ui = new UserInfo(u.getId(), u.getUsername(), u.getPassword(), uc.getCellphone());
//            QueryWrapper<UserWechatAccount> uqw = new QueryWrapper<>();
//            qw.eq("id", u.getId());
//            UserWechatAccount uwa = userOpenAccountMapper.selectOne(uqw);
//            if (null != uwa) {
//                Map<String, Object> extraInfo = new HashMap<>();
//                extraInfo.put(OAuthProvider.NICK_NAME_KEY, uwa.getNickname());
//                extraInfo.put(OAuthProvider.AVATAR_URL_KEY, uwa.getAvatarUrl());
//                extraInfo.put(OAuthProvider.COUNTRY_KEY, uwa.getCountry());
//                extraInfo.put(OAuthProvider.PROVINCE_KEY, uwa.getProvince());
//                extraInfo.put(OAuthProvider.CITY_KEY, uwa.getCity());
//                extraInfo.put(OAuthProvider.LANGUAGE_KEY, uwa.getLanguage());
//                extraInfo.put(OAuthProvider.PHONE_NUMBER_KEY, uc.getCellphone());
//                ui.setExtraInfo(extraInfo);
//            }
            return ui;
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
    public Serializable createUserInfo(String username, String password, String[] permissions, String[]
            roles, Map<String, Object> params) throws RestletException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if (params != null) {
            user.setNickname(params.getOrDefault(OAuthProvider.NICK_NAME_KEY, "username").toString());
            user.setStatus(Short.parseShort(params.getOrDefault("status", "0").toString()));
        }
        userMapper.insert(user);
        if (params != null && params.containsKey(OAuthProvider.PHONE_NUMBER_KEY) && Validator.notEmpty(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString())) {
            UserCellphone uc = new UserCellphone();
            userCellphoneMapper.insert(new UserCellphone(user.getId(), params.get(OAuthProvider.PHONE_NUMBER_KEY).toString()));
        }
        return user.getId();
    }

    @Override
    public void updateUserInfo(String username, String password, String[] permissions, String[]
            roles, Map<String, Object> params) throws RestletException {
        User user = new User();
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        user.setUpdated(new Timestamp(System.currentTimeMillis()));
        if (params != null) {
            user.setNickname(params.getOrDefault(OAuthProvider.NICK_NAME_KEY, "username").toString());
            user.setStatus(Short.parseShort(params.getOrDefault("status", "0").toString()));
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        userMapper.update(user, qw);
        if (params != null && params.containsKey(OAuthProvider.PHONE_NUMBER_KEY) && Validator.notEmpty(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString())) {
            UserCellphone uc = userCellphoneMapper.selectById(user.getId());
            if (null == uc) {
                userCellphoneMapper.insert(new UserCellphone(user.getId(), params.get(OAuthProvider.PHONE_NUMBER_KEY).toString()));
            } else {
                uc.setCellphone(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString());
                userCellphoneMapper.updateById(uc);
            }

        }
    }

    @Override
    public OAuthInfo resolve(String provider, String openId, Serializable userId, Map<String, Object> params) throws
            RestletException {
        log.debug("resolve:>>> {}, {} ", provider, openId);
//        Assert.equals(provider, WechatOAuthServiceProvider.SLUG, RestletException.badRequest("unknown provider: " + provider));
        QueryWrapper<UserOpenAccount> qw = new QueryWrapper<>();
        qw.eq("provider",provider);
        if (userId != null) {
            qw.eq("user_id", userId);
        } else {
            qw.eq("open_id", openId);
        }
        UserOpenAccount uwa = userOpenAccountMapper.selectOne(qw);
        if (null == uwa) {
            return null;
        } else if (!uwa.getOpenId().equals(openId)) {
            throw RestletException.conflict("user '" + userId + "' already linked with '" + uwa.getOpenId() + "'");
        }
        User user = userMapper.selectById(uwa.getId());
        if (null == user) {
            return null;
        }
        Map<String, Object> extraInfo = new HashMap<>(8);
        UserCellphone uc = userCellphoneMapper.selectById(user.getId());
        String cellphone = uc != null ? uc.getCellphone() : null;
        extraInfo.put(OAuthProvider.PROVIDER_KEY, uwa.getProvider());
        extraInfo.put(OAuthProvider.NICK_NAME_KEY, uwa.getNickname());
        extraInfo.put(OAuthProvider.AVATAR_URL_KEY, uwa.getAvatarUrl());
        extraInfo.put(OAuthProvider.COUNTRY_KEY, uwa.getCountry());
        extraInfo.put(OAuthProvider.PROVINCE_KEY, uwa.getProvince());
        extraInfo.put(OAuthProvider.CITY_KEY, uwa.getCity());
        extraInfo.put(OAuthProvider.LANGUAGE_KEY, uwa.getLanguage());
        extraInfo.put(OAuthProvider.PHONE_NUMBER_KEY, cellphone);
//        return new UserInfo(user.getId(), user.getUsername(), user.getPassword(), cellphone, extraInfo);
        return new OAuthInfo(uwa.getId(),uwa.getUserId(), uwa.getProvider(),uwa.getOpenId(),extraInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuthInfo register(String provider, String openId, String username, String
            password, Map<String, Object> params) throws RestletException {
//        Assert.equals(provider, WechatOAuthServiceProvider.SLUG, RestletException.badRequest("unknown provider: " + provider));
        QueryWrapper<User> qw = new QueryWrapper<>();
        Assert.notEmpty(openId, RestletException.badRequest("openId required"));
        QueryWrapper<UserOpenAccount> qwu = new QueryWrapper<>();
        qwu.eq("provider", provider);
        qwu.eq("open_id", openId);
        if (userOpenAccountMapper.selectCount(qwu) > 0) {
            throw RestletException.conflict("open_id '" + openId + "' of '"+provider+"'already exists");
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
            user.setPassword(password == null ? QuickHash.md5().hash(openId).hex() : password);
            if (params != null) {
                user.setNickname(params.getOrDefault(OAuthProvider.NICK_NAME_KEY, openId).toString());
//                user.setCellphone(params.getOrDefault(OAuthProvider.PHONE_NUMBER_KEY, "").toString());
            }
            user.setStatus((short) 1);
            userMapper.insert(user);
            if (params != null && params.containsKey(OAuthProvider.PHONE_NUMBER_KEY) && Validator.notEmpty(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString())) {
                userCellphoneMapper.insert(new UserCellphone(user.getId(), params.get(OAuthProvider.PHONE_NUMBER_KEY).toString()));
            }
            user = userMapper.selectOne(qw);
            if (null == user) {
                throw RestletException.internalServerError("register user failed");
            }
            log.info("Created user {}, linking with {} now ...", username, openId);
        } else {
            log.info("User {} exists, linking with {} now ...", username, openId);
        }
        UserOpenAccount uwa = new UserOpenAccount();
        uwa.setUserId(user.getId());
        uwa.setOpenId(openId);
        uwa.setProvider(provider);
        if (params != null) {
            uwa.setNickname(params.getOrDefault(OAuthProvider.NICK_NAME_KEY, openId).toString());
            uwa.setAvatarUrl(params.getOrDefault(OAuthProvider.AVATAR_URL_KEY, "").toString());
            uwa.setCity(params.getOrDefault(OAuthProvider.CITY_KEY, "SZ").toString());
            uwa.setProvince(params.getOrDefault(OAuthProvider.PROVINCE_KEY, "GD").toString());
            uwa.setCountry(params.getOrDefault(OAuthProvider.COUNTRY_KEY, "CN").toString());
            uwa.setLanguage(params.getOrDefault(OAuthProvider.LANGUAGE_KEY, "zh-CN").toString());
        }
        userOpenAccountMapper.insert(uwa);
        Map<String, Object> extraInfo = new HashMap<>();
        UserCellphone uc = userCellphoneMapper.selectById(user.getId());
        String cellphone = uc != null ? uc.getCellphone() : null;
        extraInfo.put(OAuthProvider.PROVIDER_KEY, uwa.getProvider());
        extraInfo.put(OAuthProvider.NICK_NAME_KEY, uwa.getNickname());
        extraInfo.put(OAuthProvider.AVATAR_URL_KEY, uwa.getAvatarUrl());
        extraInfo.put(OAuthProvider.COUNTRY_KEY, uwa.getCountry());
        extraInfo.put(OAuthProvider.PROVINCE_KEY, uwa.getProvince());
        extraInfo.put(OAuthProvider.CITY_KEY, uwa.getCity());
        extraInfo.put(OAuthProvider.LANGUAGE_KEY, uwa.getLanguage());
        extraInfo.put(OAuthProvider.PHONE_NUMBER_KEY, cellphone);
//        return new UserInfo(user.getId(), user.getUsername(), user.getPassword(), cellphone, extraInfo);
        return new OAuthInfo(uwa.getId(),uwa.getUserId(),uwa.getProvider(), uwa.getOpenId(),extraInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuthInfo update(String provider, String openId, Serializable userId, Map<String, Object> params) throws RestletException {
        Assert.equals(provider, WechatOAuthServiceProvider.SLUG, RestletException.badRequest("unknown provider: " + provider));
        Assert.notEmpty(openId, RestletException.badRequest("openId required"));
        Assert.notEmpty(params, RestletException.badRequest("params can not be empty"));
        log.debug("update:> {}, {}, {}", openId, userId, params);
        QueryWrapper<UserOpenAccount> qwu = new QueryWrapper<>();
        qwu.select("id", "user_id","provider", "open_id");
        qwu.eq("provider", provider);
        qwu.eq("open_id", openId);
        UserOpenAccount uwa1 = userOpenAccountMapper.selectOne(qwu);
//        if (uwa1 == null) {
//            return null;
//        }
        boolean updated = false;
        UserOpenAccount uwa = new UserOpenAccount();
        if (params.containsKey(OAuthProvider.NICK_NAME_KEY)) {
            //"nickname"
            uwa.setNickname(params.get(OAuthProvider.NICK_NAME_KEY).toString());
            updated = true;
        }
        if (params.containsKey(OAuthProvider.AVATAR_URL_KEY)) {
            // "headimgurl"
            uwa.setAvatarUrl(params.get(OAuthProvider.AVATAR_URL_KEY).toString());
            updated = true;
        }
        if (params.containsKey(OAuthProvider.LANGUAGE_KEY)) {
            // "language"
            uwa.setLanguage(params.get(OAuthProvider.LANGUAGE_KEY).toString());
            updated = true;
        }
        if (params.containsKey(OAuthProvider.COUNTRY_KEY)) {
            // "country"
            uwa.setCountry(params.get(OAuthProvider.COUNTRY_KEY).toString());
            updated = true;
        }
        if (params.containsKey(OAuthProvider.PROVINCE_KEY)) {
            // "province"
            uwa.setProvince(params.get(OAuthProvider.PROVINCE_KEY).toString());
            updated = true;
        }
        if (params.containsKey(OAuthProvider.CITY_KEY)) {
            // "city"
            uwa.setCity(params.get(OAuthProvider.CITY_KEY).toString());
            updated = true;
        }
        if (null != uwa1) {
            uwa.setUserId(uwa1.getUserId());
        } else if (null != userId) {
            uwa.setUserId((long) userId);
        }
        if (updated) {
            uwa.setUpdated(new Timestamp(System.currentTimeMillis()));
            if (null == uwa1) {
                uwa.setOpenId(openId);
                if (null == uwa.getId() || Validator.isEmpty(uwa.getOpenId())) {
                    throw RestletException.badRequest("openId and userId required");
                } else {
                    userOpenAccountMapper.insert(uwa);
                    uwa1 = uwa;
                }
            } else {
                userOpenAccountMapper.update(uwa, qwu);
            }
        }

        if (params.containsKey(OAuthProvider.PHONE_NUMBER_KEY)
                && params.get(OAuthProvider.PHONE_NUMBER_KEY) != null
                && Validator.notEmpty(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString())) {
            UserCellphone uc = userCellphoneMapper.selectById(uwa.getUserId());
            if (null == uc) {
                userCellphoneMapper.insert(new UserCellphone(uwa.getUserId(), params.get(OAuthProvider.PHONE_NUMBER_KEY).toString()));
            } else {
                uc.setCellphone(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString());
                userCellphoneMapper.updateById(uc);
            }
        }
        return resolve(provider, openId,userId,params);
//        return getUserById(uwa.getUserId(), false);
    }
}
