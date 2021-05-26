package org.extvos.auth.controller;

import org.extvos.auth.entity.Role;
import org.extvos.auth.entity.User;
import org.extvos.auth.service.UserService;
import org.extvos.auth.utils.CredentialHash;
import org.extvos.restlet.QuerySet;
import org.extvos.restlet.controller.BaseController;
import org.extvos.restlet.exception.RestletException;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 用户数据表
 *
 * @author Mingcai SHEN
 */
@Api(tags = {"用户操作"})
@RestController
@RequestMapping("/_builtin/auth/user")
@RequiresPermissions(value = {"*", "admin", "administration"}, logical = Logical.OR)
public class UserController extends BaseController<User, UserService> {

    @Autowired
    private UserService myService;

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
