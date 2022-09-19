package plus.extvos.auth.service;

import plus.extvos.auth.entity.RolePermission;
import plus.extvos.restlet.service.BaseService;

import java.util.List;

/**
 * 角色权限关联表
 *
 * @author Mingcai SHEN
 */
public interface RolePermissionService extends BaseService<RolePermission> {
    List<RolePermission> getPermissionsByRoles(List<Integer> roleIds);
}
