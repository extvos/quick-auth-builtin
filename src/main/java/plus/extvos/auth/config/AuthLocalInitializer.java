package plus.extvos.auth.config;

import com.baomidou.mybatisplus.annotation.TableName;
import plus.extvos.auth.entity.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        Connection conn = dataSource.getConnection();
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setLogWriter(dataSource.getLogWriter());
        String[] tableNames = new String[]{
                User.class.getAnnotation(TableName.class).value(),
                Role.class.getAnnotation(TableName.class).value(),
                Permission.class.getAnnotation(TableName.class).value(),
                UserPermission.class.getAnnotation(TableName.class).value(),
                UserRole.class.getAnnotation(TableName.class).value(),
                RolePermission.class.getAnnotation(TableName.class).value(),
                UserWechatAccount.class.getAnnotation(TableName.class).value(),
                UserCellphone.class.getAnnotation(TableName.class).value(),
        };
        for (int i = 0; i < tableNames.length; i++) {
            tableNames[i] = "'" + tableNames[i] + "'";
        }
        PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*)  FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = Database() AND TABLE_NAME IN (" + String.join(",", tableNames) + ");");
        ResultSet rs = statement.executeQuery();
        rs.next();
        int n = rs.getInt(1);
        rs.close();
        if (n < tableNames.length) {
            String[] sqlFiles = {"sql/0.auth-schema.sql"};
            for (String path : sqlFiles) {
                Reader reader = Resources.getResourceAsReader(path);
                ;
                //执行SQL脚本
                runner.runScript(reader);
                //关闭文件输入流
                reader.close();
            }
        }
    }
}
