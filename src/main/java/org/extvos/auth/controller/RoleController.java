package org.extvos.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.extvos.auth.entity.Permission;
import org.extvos.auth.entity.Role;
import org.extvos.auth.mapper.PermissionMapper;
import org.extvos.auth.service.PermissionService;
import org.extvos.auth.service.RoleService;
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
import java.util.LinkedList;
import java.util.List;

/**
 * 角色数据表
 *
 * @author Mingcai SHEN
 */
@Api(tags = {"角色管理"})
@RestController
@RequestMapping("/_builtin/auth/role")
@RequiresPermissions(value = {"*", "admin", "administration"}, logical = Logical.OR)
public class RoleController extends BaseController<Role, RoleService> {

    @Autowired
    private RoleService myService;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public RoleService getService() {
        return myService;
    }


    @Override
    public void postInsert(Role entity) throws RestletException {
        super.postInsert(entity);
    }

    @Override
    public void postUpdate(Serializable id, Role entity) throws RestletException {
        super.postUpdate(id, entity);
    }

    @Override
    public Role postSelect(Role entity) throws RestletException {
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.inSql("id", "SELECT permission_id FROM builtin_role_permissions WHERE role_id = " + entity.getId());
        entity.setPermissions(permissionMapper.selectList(qw).toArray(new Permission[0]));
        return super.postSelect(entity);
    }
}
