package plus.extvos.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.extvos.auth.entity.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关联表
 *
 * @author Mingcai SHEN
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Insert({"<script>",
            "INSERT INTO builtin_user_roles(user_id,role_id) ",
            "SELECT #{userId}, id FROM builtin_roles WHERE code IN (",
            "<foreach collection='roles' item='role' separator=', '>",
            "#{role}",
            "</foreach>",
            ")",
            "</script>"
    })
    void insertUserRoles(@Param("userId") Long userId, @Param("roles") String... roles);
}
