package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.web.model.users.SignInRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import it.sevenbits.spring_homework.web.security.JwtTokenService;
import it.sevenbits.spring_homework.web.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Performs login action.
 */
@RequestMapping("/login")
public class CookieSignInController {

    private final LoginService loginService;
    private final JwtTokenService tokenService;

    public CookieSignInController(final LoginService loginService, final JwtTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity create(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        User user = loginService.login(signInRequest);
        String token = tokenService.createToken(user);

        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)(tokenService.getTokenExpiredIn().toMillis() / 1000));
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

}
