package plus.extvos.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.extvos.auth.entity.UserRole;
import plus.extvos.auth.mapper.UserRoleMapper;
import plus.extvos.auth.service.UserRoleService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;

/**
 * 用户角色关联表
 *
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
