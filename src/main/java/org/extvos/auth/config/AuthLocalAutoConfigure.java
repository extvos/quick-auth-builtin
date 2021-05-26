package org.extvos.auth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mingcai SHEN
 */
@EntityScan("org.extvos.auth.entity")
@MapperScan("org.extvos.auth.mapper")
@ComponentScan("org.extvos.auth")
public class AuthLocalAutoConfigure {
}
