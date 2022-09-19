package plus.extvos.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import plus.extvos.auth.entity.RolePermission;

import java.util.List;

/**
 * 角色权限关联表
 *
 * @author Mingcai SHEN
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    @Select("SELECT BRP.role_id AS role_id, BRP.permission_id AS permission_id, " +
            "BR.id AS r_id, BR.code AS r_code, BR.name AS r_name, " +
            "BP.id AS p_id, BP.code AS p_code, BP.name AS p_name " +
            "FROM builtin_role_permissions AS BRP  " +
            "JOIN builtin_roles AS BR ON BR.id = BRP.role_id " +
            "JOIN builtin_permissions AS BP ON BP.id = BRP.permission_id " +
            "WHERE BRP.role_id IN (#{roleIds})"
    )
//    @Results(id = "rolePermission", value = {
//            @Result(property = "roleId", column = "role_id"),
//            @Result(property = "permissionId", column = "permission_id"),
//            @Result(property = "role", javaType = plus.extvos.auth.entity.Role.class, one = @One(columnPrefix = "r_")),
//            @Result(property = "permission", javaType = plus.extvos.auth.entity.Permission.class, one = @One(columnPrefix = "p_")),
//    })
    List<RolePermission> getPermissionsByRoles(@Param("roleIds") List<Integer> roleIds);
}
