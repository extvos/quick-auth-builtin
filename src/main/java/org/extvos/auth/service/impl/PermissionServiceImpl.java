package org.extvos.auth.service.impl;

import org.extvos.auth.entity.Permission;
import org.extvos.auth.mapper.PermissionMapper;
import org.extvos.auth.service.PermissionService;
import org.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mingcai SHEN
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper myMapper;

    @Override
    public PermissionMapper getMapper() {
        return myMapper;
    }
}
