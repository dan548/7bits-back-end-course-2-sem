package it.sevenbits.spring_homework.config;

import it.sevenbits.spring_homework.web.controller.BodySignInController;
import it.sevenbits.spring_homework.web.security.JwtTokenService;
import it.sevenbits.spring_homework.web.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Login configuration
 */
@Configuration
public class LoginConfig {

    /**
     * Assigns specific login controller
     * @param loginService login processing service
     * @param jwtTokenService JWT processing service
     * @return specific controller
     */
    @Bean
    public Object loginController(final LoginService loginService, final JwtTokenService jwtTokenService) {
        return new BodySignInController(loginService, jwtTokenService);
    }
}
