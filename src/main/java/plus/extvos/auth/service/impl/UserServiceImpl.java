package plus.extvos.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.extvos.auth.entity.User;
import plus.extvos.auth.entity.UserPermission;
import plus.extvos.auth.entity.UserRole;
import plus.extvos.auth.mapper.UserMapper;
import plus.extvos.auth.mapper.UserPermissionMapper;
import plus.extvos.auth.mapper.UserRoleMapper;
import plus.extvos.auth.service.UserService;
import plus.extvos.common.exception.ResultException;
import plus.extvos.restlet.service.impl.BaseServiceImpl;

import java.io.Serializable;

/**
 * @author Mingcai SHEN
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper myMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserPermissionMapper userPermissionMapper;

    @Override
    public UserMapper getMapper() {
        return myMapper;
    }

    @Override
    public int insert(User entity) throws ResultException {
        int ret = super.insert(entity);
        Integer[] permIds = entity.getPermissionIds();
        if (permIds != null && permIds.length > 0) {
            for (Integer pid : permIds) {
                UserPermission up = new UserPermission(entity.getId(), pid);
                userPermissionMapper.insert(up);
            }
        }
        Integer[] roleIds = entity.getRoleIds();
        if (roleIds != null && roleIds.length > 0) {
            for (Integer rid : roleIds) {
                UserRole ur = new UserRole(entity.getId(), rid);
                userRoleMapper.insert(ur);
            }
        }
        return ret;
    }

    @Override
    public int updateById(Serializable id, User entity) throws ResultException {
        int ret = super.updateById(id, entity);
        Integer[] permIds = entity.getPermissionIds();
        if (permIds != null) {
            QueryWrapper<UserPermission> qw = new QueryWrapper<>();
            qw.eq("user_id", id);
            userPermissionMapper.delete(qw);
            for (Integer pid : permIds) {
                UserPermission up = new UserPermission(entity.getId(), pid);
                userPermissionMapper.insert(up);
            }
        }

        Integer[] roleIds = entity.getRoleIds();
        if (roleIds != null) {
            QueryWrapper<UserRole> qw = new QueryWrapper<>();
            qw.eq("user_id", id);
            userRoleMapper.delete(qw);
            for (Integer rid : roleIds) {
                UserRole ur = new UserRole(entity.getId(), rid);
                userRoleMapper.insert(ur);
            }
        }
        return ret;
    }
}
