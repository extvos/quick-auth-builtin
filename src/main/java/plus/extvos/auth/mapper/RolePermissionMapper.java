package plus.extvos.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import plus.extvos.auth.entity.Permission;
import plus.extvos.auth.entity.RolePermission;

import java.util.List;

/**
 * 角色权限关联表
 *
 * @author Mingcai SHEN
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    @Select("SELECT id,code,name,comment,created,updated FROM builtin_permissions " +
            "WHERE id IN (SELECT permission_id FROM builtin_role_permissions WHERE role_id IN #{roleIds}))")
    List<Permission> getPermissionsByRoles(@Param("roleIds") List<Long> roleIds);
}
