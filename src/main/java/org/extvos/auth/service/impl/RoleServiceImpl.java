package org.extvos.auth.service.impl;

import org.extvos.auth.entity.Role;
import org.extvos.auth.mapper.RoleMapper;
import org.extvos.auth.service.RoleService;
import org.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mingcai SHEN
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper myMapper;

    @Override
    public RoleMapper getMapper() {
        return myMapper;
    }
}
