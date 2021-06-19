package org.extvos.auth.service.impl;

import org.extvos.auth.entity.UserRole;
import org.extvos.auth.mapper.UserRoleMapper;
import org.extvos.auth.service.UserRoleService;
import org.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联表
 * @author Mingcai SHEN
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Autowired
    private UserRoleMapper myMapper;
    
    @Override
    public UserRoleMapper getMapper() {
        return myMapper;
    }
}
