package plus.extvos.auth.config;

import com.baomidou.mybatisplus.annotation.TableName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import plus.extvos.auth.entity.*;
import plus.extvos.restlet.utils.DatabaseHelper;

import javax.sql.DataSource;

/**
 * @author Mingcai SHEN
 */
@Component
public class AuthLocalInitializer implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AuthLocalInitializer.class);

    @Autowired
    DataSource dataSource;

    @Value("${quick.auth.default-user.username:admin}")
    private String defaultUsername;

    @Value("${quick.auth.default-user.password:quick123}")
    private String defaultPassword;

    @Value("${quick.auth.default-user.nickname:quick123}")
    private String defaultNickname;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AuthLocalInitializer::run:> ...");
        DatabaseHelper dh = DatabaseHelper.with(dataSource);
        String[] tableNames = new String[]{
            User.class.getAnnotation(TableName.class).value(),
            Role.class.getAnnotation(TableName.class).value(),
            Permission.class.getAnnotation(TableName.class).value(),
            UserPermission.class.getAnnotation(TableName.class).value(),
            UserRole.class.getAnnotation(TableName.class).value(),
            RolePermission.class.getAnnotation(TableName.class).value(),
            UserOpenAccount.class.getAnnotation(TableName.class).value(),
            UserCellphone.class.getAnnotation(TableName.class).value(),
        };
        if(dh.tableAbsent(tableNames)>0){
            dh.runScriptsIfMySQL("sql/mysql/0.auth-schema.sql");
            dh.runScriptsIfPostgreSQL("sql/pg/0.auth-schema.sql");
        }
    }
}
