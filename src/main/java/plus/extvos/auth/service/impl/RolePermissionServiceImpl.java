package plus.extvos.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.extvos.auth.entity.RolePermission;
import plus.extvos.auth.mapper.RolePermissionMapper;
import plus.extvos.auth.service.RolePermissionService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;

/**
 * 角色权限关联表
 *
 * @author Mingcai SHEN
 */
@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {
    @Autowired
    private RolePermissionMapper myMapper;

    @Override
    public RolePermissionMapper getMapper() {
        return myMapper;
    }
}
