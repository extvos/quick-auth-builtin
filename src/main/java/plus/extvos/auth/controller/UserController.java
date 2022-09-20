package plus.extvos.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.extvos.auth.entity.*;
import plus.extvos.auth.service.*;
import plus.extvos.auth.utils.SessionUtil;
import plus.extvos.common.exception.ResultException;
import plus.extvos.restlet.QuerySet;
import plus.extvos.restlet.controller.BaseController;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户数据表
 *
 * @author Mingcai SHEN
 */
@Api(tags = {"用户管理"})
@RestController
@RequestMapping("/_builtin/auth/user")
@RequiresPermissions(value = {"*", "admin", "administration"}, logical = Logical.OR)
public class UserController extends BaseController<User, UserService> {

    @Autowired
    private UserService myService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserPermissionService userPermissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserEmailService userEmailService;

    @Autowired
    private UserCellphoneService userCellphoneService;

    @Autowired
    private UserOpenAccountService userOpenAccountService;

    @Autowired(required = false)
    private UserRegisterHook userRegisterHook;

    @Override
    public UserService getService() {
        return myService;
    }

    @Override
    public User preInsert(User entity) throws ResultException {
        if (userRegisterHook != null) {
            if (!userRegisterHook.preRegister(entity.getUsername(), entity.getPassword(), null, UserRegisterHook.ADMIN)) {
                throw ResultException.forbidden("not allow to create user.");
            }
        }
        return entity;
    }

    @Override
    public User preUpdate(Serializable id, User entity) throws ResultException {
        if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
            entity.setPassword(entity.getPassword());
        }
        entity.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        return entity;
    }

    @Override
    public User preUpdate(QuerySet<User> qs, User entity) throws ResultException {
        if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
            throw ResultException.forbidden("not allow to update password in batch.");
        }
        entity.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        return entity;
    }

    @Override
    public User postSelect(User entity) throws ResultException {
        String currentUsername = SessionUtil.currentUsername();
        Map<Long, List<Role>> roleMap = getUserRoles(entity.getId());
        Map<Long, List<Permission>> permissionMap = getUserPermissions(entity.getId());
        Map<Long, String> emailMap = getUserEmails(entity.getId());
        Map<Long, String> phoneMap = getUserCellphones(entity.getId());
        Map<Long, Map<String, UserOpenAccount>> openAccounts = getUserOpenAccounts(entity.getId());
        entity.setIsSelf(entity.getUsername().equals(currentUsername));
        entity.setEmail(emailMap.getOrDefault(entity.getId(), null));
        entity.setCellphone(phoneMap.getOrDefault(entity.getId(), null));
        Role[] roles = roleMap.getOrDefault(entity.getId(), new LinkedList<>()).toArray(new Role[0]);
        entity.setRoles(roles);
        entity.setRoleIds(Arrays.stream(roles).map(Role::getId).toArray(Integer[]::new));
        Permission[] perms = permissionMap.getOrDefault(entity.getId(), new LinkedList<>()).toArray(new Permission[0]);
        entity.setPermissions(perms);
        entity.setPermissionIds(Arrays.stream(perms).map(Permission::getId).toArray(Integer[]::new));
        return super.postSelect(entity);
    }

    protected Map<Long, Map<String, UserOpenAccount>> getUserOpenAccounts(Long... ids) {
        Map<Long, Map<String, UserOpenAccount>> m = new HashMap<>();
        if (ids.length < 1) {
            return m;
        }

        QueryWrapper<UserOpenAccount> qw1 = new QueryWrapper<>();
        qw1.in("user_id", Arrays.asList(ids));
        List<UserOpenAccount> urs = userOpenAccountService.selectByWrapper(qw1);
        if (urs.size() < 1) {
            return m;
        }
        for (UserOpenAccount ur : urs) {
            Map<String, UserOpenAccount> uom = m.getOrDefault(ur.getUserId(), new HashMap<>());
            uom.put(ur.getProvider(), ur);
            m.put(ur.getId(), uom);
        }
        return m;
    }

    protected Map<Long, String> getUserEmails(Long... ids) {
        Map<Long, String> m = new HashMap<>();
        if (ids.length < 1) {
            return m;
        }

        QueryWrapper<UserEmail> qw1 = new QueryWrapper<>();
        qw1.in("id", Arrays.asList(ids));
        List<UserEmail> urs = userEmailService.selectByWrapper(qw1);
        if (urs.size() < 1) {
            return m;
        }
        for (UserEmail ur : urs) {
            m.put(ur.getId(), ur.getEmail());
        }
        return m;
    }

    protected Map<Long, String> getUserCellphones(Long... ids) {
        Map<Long, String> m = new HashMap<>();
        if (ids.length < 1) {
            return m;
        }

        QueryWrapper<UserCellphone> qw1 = new QueryWrapper<>();
        qw1.in("id", Arrays.asList(ids));
        List<UserCellphone> urs = userCellphoneService.selectByWrapper(qw1);
        if (urs.size() < 1) {
            return m;
        }
        for (UserCellphone ur : urs) {
            m.put(ur.getId(), ur.getCellphone());
        }
        return m;
    }

    protected Map<Long, List<Role>> getUserRoles(Long... ids) {
        Map<Long, List<Role>> m = new HashMap<>();
        if (ids.length < 1) {
            return m;
        }
        QueryWrapper<UserRole> qw1 = new QueryWrapper<>();
        qw1.in("user_id", Arrays.asList(ids));
        List<UserRole> urs = userRoleService.selectByWrapper(qw1);
        if (urs.size() < 1) {
            return m;
        }
        QueryWrapper<Role> qw2 = new QueryWrapper<>();
        qw2.in("id", urs.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
        Map<Integer, Role> roleMap = roleService.selectByWrapper(qw2).stream().peek(o -> o.setUpdated(null)).peek(o -> o.setCreated(null)).collect(Collectors.toMap(Role::getId, o -> o));

        for (UserRole ur : urs) {
            List<Role> rs = m.getOrDefault(ur.getUserId(), new LinkedList<>());
            rs.add(roleMap.get(ur.getRoleId()));
            m.put(ur.getUserId(), rs);
        }
        return m;
    }

    protected Map<Long, List<Permission>> getUserPermissions(Long... ids) {
        Map<Long, List<Permission>> m = new HashMap<>();
        if (ids.length < 1) {
            return m;
        }
        QueryWrapper<UserPermission> qw1 = new QueryWrapper<>();
        qw1.in("user_id", Arrays.asList(ids));
        List<UserPermission> urs = userPermissionService.selectByWrapper(qw1);
        if (urs.size() < 1) {
            return m;
        }
        QueryWrapper<Permission> qw2 = new QueryWrapper<>();
        qw2.in("id", urs.stream().map(UserPermission::getPermissionId).collect(Collectors.toList()));
        Map<Integer, Permission> roleMap = permissionService.selectByWrapper(qw2).stream().peek(o -> o.setUpdated(null)).peek(o -> o.setCreated(null)).collect(Collectors.toMap(Permission::getId, o -> o));
        for (UserPermission ur : urs) {
            List<Permission> rs = m.getOrDefault(ur.getUserId(), new LinkedList<>());
            rs.add(roleMap.get(ur.getPermissionId()));
            m.put(ur.getUserId(), rs);
        }
        return m;
    }

    @Override
    public List<User> postSelect(List<User> entities) throws ResultException {
        String currentUsername = SessionUtil.currentUsername();
        Long[] ids = entities.stream().map(User::getId).toArray(Long[]::new);
        Map<Long, List<Role>> roleMap = getUserRoles(ids);
        Map<Long, List<Permission>> permissionMap = getUserPermissions(ids);
        Map<Long, String> emailMap = getUserEmails(ids);
        Map<Long, String> phoneMap = getUserCellphones(ids);
        for (User user : entities) {
            user.setIsSelf(user.getUsername().equals(currentUsername));
            user.setEmail(emailMap.getOrDefault(user.getId(), null));
            user.setCellphone(phoneMap.getOrDefault(user.getId(), null));
            Role[] roles = roleMap.getOrDefault(user.getId(), new LinkedList<>()).toArray(new Role[0]);
            user.setRoles(roles);
            user.setRoleIds(Arrays.stream(roles).map(Role::getId).toArray(Integer[]::new));
            Permission[] perms = permissionMap.getOrDefault(user.getId(), new LinkedList<>()).toArray(new Permission[0]);
            user.setPermissions(perms);
            user.setPermissionIds(Arrays.stream(perms).map(Permission::getId).toArray(Integer[]::new));
        }
        return super.postSelect(entities);
    }

    @Override
    public void postInsert(User entity) throws ResultException {
        if (userRegisterHook != null) {
            userRegisterHook.postRegister(entity.getId(), UserRegisterHook.ADMIN);
        }
        super.postInsert(entity);
    }

    @Override
    public void postUpdate(Serializable id, User entity) throws ResultException {
        super.postUpdate(id, entity);
    }
}
