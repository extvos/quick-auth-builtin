package plus.extvos.auth.mapper;

import plus.extvos.auth.entity.User;
import plus.extvos.auth.entity.UserOpenAccount;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;

/**
 * @author shenmc
 */
@Mapper
public interface QuickAuthMapper {
    /**
     * Get user by username
     *
     * @param username as String
     * @return User or null
     */
    @Select("SELECT U.id,U.username,U.password,C.cellphone,E.email,U.status " +
            "FROM builtin_users AS U " +
            "LEFT JOIN builtin_user_cellphones AS C ON C.id = U.id " +
            "LEFT JOIN builtin_user_emails AS E ON E.id = U.id " +
            "WHERE U.username = #{username}")
    @Results(
            {
                    @Result(property = "id", column = "id", id = true),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "cellphone", column = "cellphone"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "status", column = "status")
            }
    )
    User getUserByName(@Param("username") String username);


    /**
     * Get user by user id
     *
     * @param id of user
     * @return User or null
     */
    @Select("SELECT U.id,U.username,U.password,C.cellphone,E.email,U.status " +
            "FROM builtin_users AS U " +
            "LEFT JOIN builtin_user_cellphones AS C ON C.id = U.id " +
            "LEFT JOIN builtin_user_emails AS E ON E.id = U.id " +
            "WHERE U.id = #{id}")
    @Results(
            {
                    @Result(property = "id", column = "id", id = true),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "cellphone", column = "cellphone"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "status", column = "status")
            }
    )
    User getUserById(@Param("id") Serializable id);

    /**
     * Get user by phone
     *
     * @param phone as String
     * @return User or null
     */
    @Select("SELECT U.id,U.username,U.password,C.cellphone,E.email,U.status " +
            "FROM builtin_users AS U " +
            "LEFT JOIN builtin_user_cellphones AS C ON C.id = U.id " +
            "LEFT JOIN builtin_user_emails AS E ON E.id = U.id " +
            "WHERE C.cellphone = #{phone}")
    @Results(
            {
                    @Result(property = "id", column = "id", id = true),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "cellphone", column = "cellphone"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "status", column = "status")
            }
    )
    User getUserByPhone(@Param("phone") String phone);

    /**
     * Get user by email
     *
     * @param email as String
     * @return User or null
     */
    @Select("SELECT U.id,U.username,U.password,C.cellphone,E.email,U.status " +
            "FROM builtin_users AS U " +
            "LEFT JOIN builtin_user_cellphones AS C ON C.id = U.id " +
            "LEFT JOIN builtin_user_emails AS E ON E.id = U.id " +
            "WHERE E.email = #{email}")
    @Results(
            {
                    @Result(property = "id", column = "id", id = true),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "cellphone", column = "cellphone"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "status", column = "status")
            }
    )
    User getUserByEmail(@Param("email") String email);

    /**
     * Resolve user by open account
     *
     * @param provider slug
     * @param openId   if from provider
     * @param userId   if presented
     * @return UserOpenAccount with user
     */
    @Select({
            "<script>",
            "  SELECT U.id, U.username, U.password, C.cellphone,E.email,U.status, ",
            "         O.provider, O.open_id, O.user_id, O.nickname, O.language, O.city, O.province, O.country, O.avatar_url, O.extras ",
            "  FROM builtin_users AS U ",
            "  LEFT JOIN builtin_user_open_accounts AS O on O.user_id = U.id ",
            "  LEFT JOIN builtin_user_cellphones AS C ON C.id = U.id ",
            "  LEFT JOIN builtin_user_emails AS E ON E.id = U.id ",
            "  <choose>",
            "    <when test = \" userId != null \">",
            "      where U.id =#{userId}",
            "    </when>",
            "    <otherwise>",
            "      WHERE O.open_id = #{openId}",
            "    </otherwise>",
            "  </choose>",
            "</script>"
    })
    @Results(
            {
                    @Result(property = "user.id", column = "id", id = true),
                    @Result(property = "user.username", column = "username"),
                    @Result(property = "user.password", column = "password"),
                    @Result(property = "user.cellphone", column = "cellphone"),
                    @Result(property = "user.email", column = "email"),
                    @Result(property = "user.status", column = "status"),
                    @Result(property = "provider", column = "provider"),
                    @Result(property = "openId", column = "open_id"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "nickname", column = "nickname"),
                    @Result(property = "language", column = "language"),
                    @Result(property = "city", column = "city"),
                    @Result(property = "province", column = "province"),
                    @Result(property = "country", column = "country"),
                    @Result(property = "avatarUrl", column = "avatar_url"),
                    @Result(property = "extraString", column = "extras"),
            }
    )
    UserOpenAccount resolve(@Param("provider") String provider, @Param("openId") String openId, @Param("userId") Serializable userId);
}
