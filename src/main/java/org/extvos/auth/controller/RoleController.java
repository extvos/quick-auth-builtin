package org.extvos.auth.controller;

import org.extvos.auth.entity.Role;
import org.extvos.auth.service.RoleService;
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
 * 角色数据表
 *
 * @author Mingcai SHEN
 */
@Api(tags = {"角色操作"})
@RestController
@RequestMapping("/_builtin/auth/role")
@RequiresPermissions(value = {"*","admin","administration"},logical = Logical.OR)
public class RoleController extends BaseController<Role, RoleService> {

    @Autowired
    private RoleService myService;

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
        return super.postSelect(entity);
    }
}
