package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.web.model.users.SignInResponse;
import it.sevenbits.spring_homework.web.model.users.SignInRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import it.sevenbits.spring_homework.web.security.JwtTokenService;
import it.sevenbits.spring_homework.web.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/signin")
public class BodySignInController {

    private final LoginService loginService;
    private final JwtTokenService tokenService;

    public BodySignInController(LoginService loginService, JwtTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    @PostMapping
    @ResponseBody
    public SignInResponse create(@RequestBody SignInRequest signInRequest) {
        User user = loginService.login(signInRequest);
        String token = tokenService.createToken(user);
        return new SignInResponse(token);
    }
}
