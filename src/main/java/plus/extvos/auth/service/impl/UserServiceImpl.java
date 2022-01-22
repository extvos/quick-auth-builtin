package plus.extvos.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.extvos.auth.entity.*;
import plus.extvos.auth.mapper.*;
import plus.extvos.auth.service.UserService;
import plus.extvos.common.Validator;
import plus.extvos.common.exception.ResultException;
import plus.extvos.restlet.service.impl.BaseServiceImpl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    @Autowired
    private UserEmailMapper userEmailMapper;

    @Autowired
    private UserCellphoneMapper userCellphoneMapper;

    @Autowired
    private UserOpenAccountMapper userOpenAccountMapper;

    @Override
    public UserMapper getMapper() {
        return myMapper;
    }

    @Override
    public boolean parseQuery(String k, Object v, QueryWrapper<?> wrapper) {
        if (Validator.isEmpty(k) || Validator.isNull(v) || Validator.isEmpty(v.toString())) {
            return true;
        }
        switch (k) {
            case "role":
                wrapper.inSql("id", "SELECT user_id FROM builtin_user_roles AS bur JOIN builtin_roles AS br ON br.id = bur.role_id WHERE br.code = '" + v + "'");
                return true;
            case "roleId":
            case "role_id":
                wrapper.inSql("id", "SELECT user_id FROM builtin_user_roles WHERE role_id = " + v);
                return true;
            case "role__in":
                String ss = Arrays.stream(v.toString().split(",")).map(s -> "'" + s + "'").collect(Collectors.joining(","));
                wrapper.inSql("id", "SELECT user_id FROM builtin_user_roles AS bur JOIN builtin_roles AS br ON br.id = bur.role_id WHERE br.code IN (" + ss + ")");
                return true;
            case "roleId__in":
            case "role_id__in":
                wrapper.inSql("id", "SELECT user_id FROM builtin_user_roles WHERE role_id IN (" + v + ")");
                return true;
            default:
                return false;
        }
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
        if (null != entity.getEmail()) {
            userEmailMapper.insert(new UserEmail(entity.getId(), entity.getEmail()));
        }
        if (null != entity.getCellphone()) {
            userCellphoneMapper.insert(new UserCellphone(entity.getId(), entity.getCellphone()));
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
        if (null != entity.getEmail()) {
            if (null == userEmailMapper.selectById(entity.getId())) {
                userEmailMapper.insert(new UserEmail(entity.getId(), entity.getEmail()));
            } else {
                userEmailMapper.updateById(new UserEmail(entity.getId(), entity.getEmail()));
            }
        }
        if (null != entity.getCellphone()) {
            if (null == userCellphoneMapper.selectById(entity.getId())) {
                userCellphoneMapper.insert(new UserCellphone(entity.getId(), entity.getCellphone()));
            } else {
                userCellphoneMapper.updateById(new UserCellphone(entity.getId(), entity.getCellphone()));
            }
        }
        return ret;
    }
}
