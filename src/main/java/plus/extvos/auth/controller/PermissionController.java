package plus.extvos.auth.controller;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.extvos.auth.entity.Permission;
import plus.extvos.auth.service.PermissionService;
import plus.extvos.restlet.controller.BaseController;

/**
 * 权限数据表
 *
 * @author Mingcai SHEN
 */
@Api(tags = {"权限管理"})
@RestController
@RequestMapping("/_builtin/auth/permission")
@RequiresPermissions(value = {"*", "admin", "administration"}, logical = Logical.OR)
public class PermissionController extends BaseController<Permission, PermissionService> {

    @Autowired
    private PermissionService myService;

    @Override
    public PermissionService getService() {
        return myService;
    }

}
