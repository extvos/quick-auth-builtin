package plus.extvos.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plus.extvos.auth.config.QuickAuthConfig;
import plus.extvos.auth.dto.OAuthInfo;
import plus.extvos.auth.dto.PermissionInfo;
import plus.extvos.auth.dto.RoleInfo;
import plus.extvos.auth.dto.UserInfo;
import plus.extvos.auth.entity.*;
import plus.extvos.auth.enums.AuthCode;
import plus.extvos.auth.mapper.*;
import plus.extvos.auth.service.*;
import plus.extvos.common.Assert;
import plus.extvos.common.Validator;
import plus.extvos.common.exception.ResultException;

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
    private UserPermissionMapper userPermissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserOpenAccountMapper userOpenAccountMapper;

    @Autowired
    UserCellphoneMapper userCellphoneMapper;

    @Autowired
    UserEmailMapper userEmailMapper;

    @Autowired
    QuickAuthMapper quickAuthMapper;

    @Autowired(required = false)
    private UserRegisterHook userRegisterHook;

    @Autowired
    private QuickAuthConfig authConfig;


    @Override
    public UserInfo getUserByName(String name, boolean checkEnabled) throws ResultException {
        User u = quickAuthMapper.getUserByName(name);
        if (null == u) {
            return null;
        } else if (checkEnabled && u.getStatus() < 1) {
            if (u.getStatus() == 0) {
                throw new ResultException(AuthCode.ACCOUNT_DISABLED, "user not activated");
            } else {
                throw new ResultException(AuthCode.ACCOUNT_LOCKED, "user locked");
            }
        } else {
            return new UserInfo(u.getId(), u.getUsername(), u.getPassword(), u.getCellphone(), u.getEmail());
        }
    }

    @Override
    public UserInfo getUserById(Serializable id, boolean checkEnabled) throws ResultException {
        User u = quickAuthMapper.getUserById(id);
        if (null == u) {
            return null;
        } else if (checkEnabled && u.getStatus() < 1) {
            if (u.getStatus() == 0) {
                throw ResultException.forbidden("user not activated");
            } else {
                throw ResultException.forbidden("user was locked");
            }
        } else {
            return new UserInfo(u.getId(), u.getUsername(), u.getPassword(), u.getCellphone(), u.getEmail());
        }
    }

    @Override
    public UserInfo getUserByPhone(String phone, boolean checkEnabled) throws ResultException {
        User u = quickAuthMapper.getUserByPhone(phone);
        if (null == u) {
            return null;
        } else if (checkEnabled && u.getStatus() < 1) {
            if (u.getStatus() == 0) {
                throw ResultException.forbidden("user not activated");
            } else {
                throw ResultException.forbidden("user was locked");
            }
        } else {
            return new UserInfo(u.getId(), u.getUsername(), u.getPassword(), u.getCellphone(), u.getEmail());
        }
    }

    @Override
    public UserInfo getUserByEmail(String email, boolean checkEnabled) throws ResultException {
        User u = quickAuthMapper.getUserByEmail(email);
        if (null == u) {
            return null;
        } else if (checkEnabled && u.getStatus() < 1) {
            if (u.getStatus() == 0) {
                throw ResultException.forbidden("user not activated");
            } else {
                throw ResultException.forbidden("user was locked");
            }
        } else {
            return new UserInfo(u.getId(), u.getUsername(), u.getPassword(), u.getCellphone(), u.getEmail());
        }
    }

    @Override
    public List<RoleInfo> getRoles(Serializable id) throws ResultException {
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
    public List<PermissionInfo> getPermissions(Serializable id) throws ResultException {
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
    public UserInfo fillUserInfo(UserInfo userInfo) throws ResultException {
        if (null != userInfo && null != userInfo.getUserId()) {
            List<RoleInfo> roles = getRoles(userInfo.getUserId());
            List<String> roleCodes = new LinkedList<>();
            roles.forEach(role -> {
                roleCodes.add(role.getCode());
            });
            List<PermissionInfo> perms = getPermissions(userInfo.getUserId());
            List<String> permCodes = new LinkedList<>();
            perms.forEach(role -> {
                permCodes.add(role.getCode());
            });
            userInfo.setRoles(roleCodes.toArray(new String[0]));
            userInfo.setPermissions(permCodes.toArray(new String[0]));
        }
        return userInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Serializable createUserInfo(String username, String password, short status, String[] permissions, String[]
            roles, Map<String, Object> params) throws ResultException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setStatus(status);
        if (params != null) {
            user.setNickname(params.getOrDefault(OAuthProvider.NICK_NAME_KEY, username).toString());
        } else {
            user.setNickname(username);
        }
        userMapper.insert(user);
        if (params != null && params.containsKey(OAuthProvider.PHONE_NUMBER_KEY) && Validator.notEmpty(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString())) {
            UserCellphone uc = new UserCellphone();
            userCellphoneMapper.insert(new UserCellphone(user.getId(), params.get(OAuthProvider.PHONE_NUMBER_KEY).toString()));
        }

        if (params != null && params.containsKey(OAuthProvider.EMAIL) && Validator.notEmpty(params.get(OAuthProvider.EMAIL).toString())) {
            UserCellphone uc = new UserCellphone();
            userEmailMapper.insert(new UserEmail(user.getId(), params.get(OAuthProvider.EMAIL).toString()));
        }
        if (permissions != null && permissions.length > 0) {
            userPermissionMapper.insertUserPermission(user.getId(), permissions);
        }
        if (roles != null && roles.length > 0) {
            userRoleMapper.insertUserRoles(user.getId(), roles);
        }
        return user.getId();
    }

    @Override
    public void updateUserInfo(String username, String password, String[] permissions, String[]
            roles, Map<String, Object> params) throws ResultException {
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
        if (permissions != null && permissions.length > 0) {
            QueryWrapper<UserPermission> pqw = new QueryWrapper<>();
            qw.eq("user_id", user.getId());
            userPermissionMapper.delete(pqw);
            userPermissionMapper.insertUserPermission(user.getId(), permissions);
        }
        if (roles != null && roles.length > 0) {
            QueryWrapper<UserRole> pqw = new QueryWrapper<>();
            qw.eq("user_id", user.getId());
            userRoleMapper.delete(pqw);
            userRoleMapper.insertUserRoles(user.getId(), roles);
        }
    }

    @Override
    public OAuthInfo resolve(String provider, String openId, String unionId, Serializable userId, Map<String, Object> params) throws
            ResultException {
        log.debug("resolve:>>> {}, {} ", provider, openId);
        QueryWrapper<UserOpenAccount> qw = new QueryWrapper<>();
        qw.eq("provider", provider);
        qw.eq("open_id", openId);
        UserOpenAccount uwa = userOpenAccountMapper.selectOne(qw);
        if (null == uwa) {
            return null;
        } else if (userId != null && !uwa.getUserId().equals(Long.parseLong(userId.toString()))) {
            throw ResultException.conflict(provider + " user '" + openId + "' already linked with another user!");
        }
        User user = userMapper.selectById(uwa.getUserId());
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
        return new OAuthInfo(uwa.getId(), uwa.getUserId(), uwa.getProvider(), uwa.getOpenId(), unionId, extraInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuthInfo register(String provider, String openId, String unionId, String username, String
            password, Map<String, Object> params) throws ResultException {
        QueryWrapper<User> qw = new QueryWrapper<>();
        Assert.notEmpty(openId, ResultException.badRequest("openId required"));
        QueryWrapper<UserOpenAccount> qwu = new QueryWrapper<>();
        qwu.eq("provider", provider);
        qwu.eq("open_id", openId);
        if (userOpenAccountMapper.selectCount(qwu) > 0) {
            throw ResultException.conflict(provider + " user '" + openId + "' already exists");
        }

        User user = null;
        // 手机可能已经存在了
        if (params != null && params.containsKey(OAuthProvider.PHONE_NUMBER_KEY) && Validator.notEmpty(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString())) {
            String cellphone = params.get(OAuthProvider.PHONE_NUMBER_KEY).toString();
            UserCellphone userCellphone = userCellphoneMapper.selectOne(new QueryWrapper<UserCellphone>().eq("cellphone", cellphone));
            if (userCellphone != null) {
                user = userMapper.selectOne(new QueryWrapper<User>().eq("id", userCellphone.getId()));
            }
        }
        if (user == null) {
            Assert.notEmpty(username, ResultException.badRequest("username required"));
            qw.eq("username", username);
            user = userMapper.selectOne(qw);
        }
        Assert.notNull(user, ResultException.forbidden("user not exists"));
//        if (user == null) {
//            log.info("User {} not exists, register now ...", username);
//            user = new User();
//            String[] perms = null;
//            String[] roles = null;
//            short status = 0;
////            user.setUsername(username);
//            password = password == null ? QuickHash.md5().hash(openId).hex() : password;
////            user.setPassword(password == null ? QuickHash.md5().hash(openId).hex() : password);
////            if (params != null) {
////                user.setNickname(params.getOrDefault(OAuthProvider.NICK_NAME_KEY, openId).toString());
////                user.setCellphone(params.getOrDefault(OAuthProvider.PHONE_NUMBER_KEY, "").toString());
////            }
//            if (userRegisterHook != null) {
//                if (!userRegisterHook.preRegister(username, password, params, UserRegisterHook.OAUTH)) {
//                    throw ResultException.forbidden("not allowed to register user");
//                }
//                perms = userRegisterHook.defaultPermissions(UserRegisterHook.OAUTH);
//                roles = userRegisterHook.defaultRoles(UserRegisterHook.OAUTH);
//                status = userRegisterHook.defaultStatus(UserRegisterHook.OAUTH);
//            } else {
//                status = authConfig.getDefaultStatus();
//            }
////            user.setStatus((short) 1);
//            Serializable userId = createUserInfo(username, password, status, perms, roles, params);
////            userMapper.insert(user);
////            if (params != null && params.containsKey(OAuthProvider.PHONE_NUMBER_KEY) && Validator.notEmpty(params.get(OAuthProvider.PHONE_NUMBER_KEY).toString())) {
////                String cellphone = params.get(OAuthProvider.PHONE_NUMBER_KEY).toString();
////                userCellphoneMapper.insert(new UserCellphone(Long.parseLong(userId.toString()), cellphone));
////            }
//            user = userMapper.selectOne(qw);
//            if (null == user) {
//                throw ResultException.internalServerError("register user failed");
//            }
//            log.info("Created user {}, linking with {} now ...", username, openId);
//        } else {
//            log.info("User {} exists, linking with {} now ...", username, openId);
//        }
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
        UserOpenAccount userOpenAccount = userOpenAccountMapper.selectOne(new QueryWrapper<UserOpenAccount>()
                .eq("provider", provider)
                .eq("user_id", user.getId()));
        if (userOpenAccount != null) {
            uwa.setId(userOpenAccount.getId());
            userOpenAccountMapper.updateById(uwa);
        } else {
            userOpenAccountMapper.insert(uwa);
        }
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
        return new OAuthInfo(uwa.getId(), uwa.getUserId(), uwa.getProvider(), uwa.getOpenId(), null, extraInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuthInfo update(String provider, String openId, String unionId, Serializable userId, Map<String, Object> params) throws ResultException {
        //Assert.equals(provider, WechatOAuthServiceProvider.SLUG, RestletException.badRequest("unknown provider: " + provider));
        Assert.notEmpty(openId, ResultException.badRequest("openId required"));
        Assert.notEmpty(params, ResultException.badRequest("params can not be empty"));
        log.debug("update:> {}, {}, {}", openId, userId, params);
        QueryWrapper<UserOpenAccount> qwu = new QueryWrapper<>();
        qwu.select("id", "user_id", "provider", "open_id");
        qwu.eq("provider", provider);
        qwu.eq("open_id", openId);
        UserOpenAccount uwa1 = userOpenAccountMapper.selectOne(qwu);
//        if (uwa1 == null) {
//            return null;
//        }
        boolean updated = false;
        UserOpenAccount uwa = new UserOpenAccount();
        if (uwa1 == null) {
            uwa.setProvider(provider);
            uwa.setUserId(userId instanceof Long ? (Long) userId : Long.parseLong(userId.toString()));
            uwa.setOpenId(openId);
        }
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
                if (null == uwa.getUserId() || Validator.isEmpty(uwa.getOpenId())) {
                    throw ResultException.badRequest("openId and userId required");
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
        return resolve(provider, openId, unionId, userId, params);
//        return getUserById(uwa.getUserId(), false);
    }
}
