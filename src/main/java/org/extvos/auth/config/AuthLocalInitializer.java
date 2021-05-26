package org.extvos.auth.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Reader;
import java.sql.Connection;

/**
 * @author Mingcai SHEN
 */
@Component
public class AuthLocalInitializer implements CommandLineRunner {
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
    public void run(String... args) throws Exception {
        log.info("AuthLocalInitializer::run:> ...");
        Connection conn = dataSource.getConnection();
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setLogWriter(dataSource.getLogWriter());
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
