package org.extvos.auth.service.impl;

import org.extvos.auth.entity.RolePermission;
import org.extvos.auth.mapper.RolePermissionMapper;
import org.extvos.auth.service.RolePermissionService;
import org.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色权限关联表
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
