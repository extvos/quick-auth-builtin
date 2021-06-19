package org.extvos.auth.service.impl;

import org.extvos.auth.entity.UserPermission;
import org.extvos.auth.mapper.UserPermissionMapper;
import org.extvos.auth.service.UserPermissionService;
import org.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户权限关联表
 * @author Mingcai SHEN
 */
@Service
public class UserPermissionServiceImpl extends BaseServiceImpl<UserPermissionMapper, UserPermission> implements UserPermissionService {
    @Autowired
    private UserPermissionMapper myMapper;
    
    @Override
    public UserPermissionMapper getMapper() {
        return myMapper;
    }
}
