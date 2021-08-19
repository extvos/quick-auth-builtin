package plus.extvos.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.extvos.auth.entity.Role;
import plus.extvos.auth.entity.RolePermission;
import plus.extvos.auth.mapper.RoleMapper;
import plus.extvos.auth.mapper.RolePermissionMapper;
import plus.extvos.auth.service.RoleService;
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
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper myMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public RoleMapper getMapper() {
        return myMapper;
    }

    @Override
    public int insert(Role entity) throws ResultException {
        int ret = super.insert(entity);
        Integer[] permIds = entity.getPermissionIds();
        if (permIds != null && permIds.length > 0) {
            for (Integer pid : permIds) {
                RolePermission rp = new RolePermission(entity.getId(), pid);
                rolePermissionMapper.insert(rp);
            }
        }
        return ret;
    }

    @Override
    public boolean parseQuery(String k, Object v, QueryWrapper<?> wrapper) {
        if (Validator.isEmpty(k) || Validator.isNull(v) || Validator.isEmpty(v.toString())) {
            return true;
        }
        switch (k) {
            case "permission":
                wrapper.inSql("id", "SELECT role_id FROM builtin_role_permissions AS brp JOIN builtin_permissions AS bp ON bp.id = brp.permission_id WHERE bp.code = '" + v + "'");
                return true;
            case "permissionId":
            case "permission_id":
                wrapper.inSql("id", "SELECT role_id FROM builtin_role_permissions WHERE permission_id = " + v);
                return true;
            case "permission__in":
                String ss = Arrays.stream(v.toString().split(",")).map(s -> "'" + s + "'").collect(Collectors.joining(","));
                wrapper.inSql("id", "SELECT role_id FROM builtin_role_permissions AS brp JOIN builtin_permissions AS bp ON bp.id = brp.permission_id WHERE bp.code IN (" + ss + ")");
                return true;
            case "permissionId__in":
            case "permission_id__in":
                wrapper.inSql("id", "SELECT role_id FROM builtin_role_permissions WHERE permission_id IN (" + v + ")");
                return true;
            default:
                return false;
        }
    }

    @Override
    public int updateById(Serializable id, Role entity) throws ResultException {
        int ret = super.updateById(id, entity);
        Integer[] permIds = entity.getPermissionIds();
        if (permIds != null) {
            QueryWrapper<RolePermission> qw = new QueryWrapper<>();
            qw.eq("role_id", id);
            rolePermissionMapper.delete(qw);
            for (Integer pid : permIds) {
                RolePermission rp = new RolePermission(entity.getId(), pid);
                rolePermissionMapper.insert(rp);
            }
        }
        return ret;
    }
}
