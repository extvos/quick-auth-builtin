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
import plus.extvos.auth.entity.RolePermission;
import plus.extvos.auth.service.PermissionService;
import plus.extvos.auth.service.RolePermissionService;
import plus.extvos.auth.service.RoleService;
import plus.extvos.common.exception.ResultException;
import plus.extvos.restlet.QuerySet;
import plus.extvos.restlet.controller.BaseController;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色数据表
 *
 * @author Mingcai SHEN
 */
@Api(tags = {"角色管理"})
@RestController
@RequestMapping("/_builtin/auth/role")
@RequiresPermissions(value = {"*", "admin"}, logical = Logical.OR)
public class RoleController extends BaseController<Role, RoleService> {

    @Autowired
    private RoleService myService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public RoleService getService() {
        return myService;
    }


    @Override
    public void postInsert(Role entity) throws ResultException {
        super.postInsert(entity);
    }

    protected Map<Integer, List<Permission>> getRolePermissions(Integer... ids) {
        Map<Integer, List<Permission>> m = new HashMap<>();
        if (ids.length < 1) {
            return m;
        }
        QueryWrapper<RolePermission> qw1 = new QueryWrapper<>();
        qw1.in("role_id", Arrays.asList(ids));
        List<RolePermission> urs = rolePermissionService.selectByWrapper(qw1);
        if (urs.size() < 1) {
            return m;
        }
        QueryWrapper<Permission> qw2 = new QueryWrapper<>();
        qw2.in("id", urs.stream().map(RolePermission::getPermissionId).collect(Collectors.toList()));
        Map<Integer, Permission> roleMap = permissionService.selectByWrapper(qw2).stream().peek(o -> o.setUpdated(null)).peek(o -> o.setCreated(null)).collect(Collectors.toMap(Permission::getId, o -> o));
        for (RolePermission ur : urs) {
            List<Permission> rs = m.getOrDefault(ur.getRoleId(), new LinkedList<>());
            rs.add(roleMap.get(ur.getPermissionId()));
            m.put(ur.getRoleId(), rs);
        }
        return m;
    }

    @Override
    public Role preUpdate(Serializable id, Role entity) throws ResultException {
        entity.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        return super.preUpdate(id, entity);
    }

    @Override
    public Role preUpdate(QuerySet<Role> qs, Role entity) throws ResultException {
        entity.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        return super.preUpdate(qs, entity);
    }

    @Override
    public void postUpdate(Serializable id, Role entity) throws ResultException {
        super.postUpdate(id, entity);
    }

    @Override
    public Role postSelect(Role entity) throws ResultException {
        Map<Integer, List<Permission>> pm = getRolePermissions(entity.getId());
        Permission[] perms = pm.getOrDefault(entity.getId(), new LinkedList<>()).toArray(new Permission[0]);
        entity.setPermissions(perms);
        entity.setPermissionIds(Arrays.stream(perms).map(Permission::getId).toArray(Integer[]::new));
        return super.postSelect(entity);
    }

    @Override
    public List<Role> postSelect(List<Role> entities) throws ResultException {
        Integer[] ids = entities.stream().map(Role::getId).toArray(Integer[]::new);
        Map<Integer, List<Permission>> pm = getRolePermissions(ids);
        entities.forEach(e -> {
            Permission[] perms = pm.getOrDefault(e.getId(), new LinkedList<>()).toArray(new Permission[0]);
            e.setPermissions(perms);
            e.setPermissionIds(Arrays.stream(perms).map(Permission::getId).toArray(Integer[]::new));
        });
        return super.postSelect(entities);
    }
}
