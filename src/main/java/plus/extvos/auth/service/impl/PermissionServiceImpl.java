package plus.extvos.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.extvos.auth.entity.Permission;
import plus.extvos.auth.mapper.PermissionMapper;
import plus.extvos.auth.service.PermissionService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;

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
