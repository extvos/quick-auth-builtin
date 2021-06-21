package plus.extvos.auth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Mingcai SHEN
 */
@EntityScan("plus.extvos.auth.entity")
@MapperScan("plus.extvos.auth.mapper")
@ComponentScan("plus.extvos.auth")
public class AuthLocalAutoConfigure {

}
