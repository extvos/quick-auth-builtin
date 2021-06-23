package plus.extvos.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import plus.extvos.auth.entity.Permission;
import plus.extvos.auth.entity.Role;
import plus.extvos.auth.entity.User;
import plus.extvos.auth.mapper.PermissionMapper;
import plus.extvos.auth.mapper.RoleMapper;
import plus.extvos.auth.service.UserService;
import plus.extvos.restlet.QuerySet;
import plus.extvos.restlet.controller.BaseController;
import plus.extvos.restlet.exception.RestletException;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

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
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserService getService() {
        return myService;
    }

    @Override
    public User preInsert(User entity) throws RestletException {
        throw RestletException.forbidden("not allow to create user directly, use method provided in auth-base.");
    }

    @Override
    public User preUpdate(Serializable id, User entity) throws RestletException {
        if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
            entity.setPassword(entity.getPassword());
        }
        return entity;
    }

    @Override
    public User preUpdate(QuerySet<User> qs, User entity) throws RestletException {
        if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
            throw RestletException.forbidden("not allow to update password in batch.");
        }
        return entity;
    }

    @Override
    public User postSelect(User entity) throws RestletException {
        QueryWrapper<Permission> qw1 = new QueryWrapper<>();
        qw1.inSql("id", "SELECT permission_id FROM builtin_user_permissions WHERE user_id = " + entity.getId());
        entity.setPermissions(permissionMapper.selectList(qw1).toArray(new Permission[0]));
        QueryWrapper<Role> qw2 = new QueryWrapper<>();
        qw2.inSql("id", "SELECT role_id FROM builtin_user_roles WHERE user_id = " + entity.getId());
        entity.setRoles(roleMapper.selectList(qw2).toArray(new Role[0]));
        return super.postSelect(entity);
    }

    @Override
    public void postInsert(User entity) throws RestletException {
        super.postInsert(entity);
    }

    @Override
    public void postUpdate(Serializable id, User entity) throws RestletException {
        super.postUpdate(id, entity);
    }
}
