package plus.extvos.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.extvos.auth.entity.Role;
import plus.extvos.auth.entity.RolePermission;
import plus.extvos.auth.mapper.RoleMapper;
import plus.extvos.auth.mapper.RolePermissionMapper;
import plus.extvos.auth.service.RoleService;
import plus.extvos.common.exception.ResultException;
import plus.extvos.restlet.service.impl.BaseServiceImpl;

import java.io.Serializable;

/**
 * @author Mingcai SHEN
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

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
