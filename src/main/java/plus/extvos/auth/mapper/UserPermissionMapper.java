package plus.extvos.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.extvos.auth.entity.UserPermission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 用户权限关联表
 *
 * @author Mingcai SHEN
 */
public interface UserPermissionMapper extends BaseMapper<UserPermission> {
    @Insert({"<script>",
            "INSERT INTO builtin_user_permissions(user_id,permission_id) ",
            "SELECT #{userId}, id FROM builtin_permissions WHERE code IN (",
            "<foreach collection='permissions' item='perm' separator=', '>",
            "#{perm}",
            "</foreach>",
            ")",
            "</script>"
    })
    void insertUserPermission(@Param("userId") Long userId, @Param("permissions") String... permissions);
}
