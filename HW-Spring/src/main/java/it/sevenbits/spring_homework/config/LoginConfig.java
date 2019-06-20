package it.sevenbits.spring_homework.config;

import it.sevenbits.spring_homework.web.controller.BodySignInController;
import it.sevenbits.spring_homework.web.controller.CookieSignInController;
import it.sevenbits.spring_homework.web.security.JwtTokenService;
import it.sevenbits.spring_homework.web.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfig {

    @Bean
    public Object loginController(final LoginService loginService, final JwtTokenService jwtTokenService) {
//        return new CookieSignInController(loginService, jwtTokenService);
        return new BodySignInController(loginService, jwtTokenService);
    }
}
