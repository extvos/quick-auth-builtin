package plus.extvos.auth.service.impl;

import plus.extvos.auth.dto.Permission;
import plus.extvos.auth.mapper.PermissionMapper;
import plus.extvos.auth.service.PermissionService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;
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
