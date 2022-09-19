package plus.extvos.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.extvos.auth.entity.Permission;
import plus.extvos.auth.entity.Role;
import plus.extvos.auth.service.PermissionService;
import plus.extvos.auth.service.RoleService;
import plus.extvos.common.exception.ResultException;
import plus.extvos.restlet.controller.BaseController;

import java.io.Serializable;
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
    private PermissionService permissionService;

//    @Autowired
//    private RolePermissionService rolePermissionService;

    @Override
    public RoleService getService() {
        return myService;
    }


    @Override
    public void postInsert(Role entity) throws ResultException {
        super.postInsert(entity);
    }

    @Override
    public void postUpdate(Serializable id, Role entity) throws ResultException {
        super.postUpdate(id, entity);
    }

    @Override
    public Role postSelect(Role entity) throws ResultException {
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.inSql("id", "SELECT permission_id FROM builtin_role_permissions WHERE role_id = " + entity.getId());
        entity.setPermissions(permissionService.selectByWrapper(qw).toArray(new Permission[0]));
        return super.postSelect(entity);
    }

    @Override
    public List<Role> postSelect(List<Role> entities) throws ResultException {
//        List<Integer> roleIds = new ArrayList<>();
//        entities.forEach(e -> roleIds.add(e.getId()));
//        Map<Integer, List<Permission>> rolePerms = new HashMap<>();
//        List<RolePermission> rpms = rolePermissionService.getPermissionsByRoles(roleIds);
//        rpms.forEach(rp -> {
//            List<Permission> lp;
//            if (rolePerms.containsKey(rp.getRoleId())) {
//                lp = rolePerms.get(rp.getRoleId());
//            } else {
//                lp = new ArrayList<>();
//            }
//            lp.add(rp.getPermission());
//            rolePerms.put(rp.getRoleId(), lp);
//        });
//        entities.forEach(e -> {
//            if (rolePerms.containsKey(e.getId())) {
//                List<Permission> lp = rolePerms.get(e.getId());
//                Permission[] perms = new Permission[lp.size()];
//                e.setPermissions(perms);
//            }
//        });
        return super.postSelect(entities);
    }
}
