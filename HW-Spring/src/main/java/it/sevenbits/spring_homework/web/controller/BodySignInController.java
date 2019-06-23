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

/**
 * User login controller.
 */
@RequestMapping("/signin")
public class BodySignInController {

    private final LoginService loginService;
    private final JwtTokenService tokenService;

    /**
     * Constructs controller.
     * @param loginService service to get user by request
     * @param tokenService token operating service
     */
    public BodySignInController(final LoginService loginService, final JwtTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    /**
     * Creates response to a request.
     * @param signInRequest login request
     * @return server response
     */
    @PostMapping
    @ResponseBody
    public SignInResponse create(final @RequestBody SignInRequest signInRequest) {
        User user = loginService.login(signInRequest);
        String token = tokenService.createToken(user);
        return new SignInResponse(token);
    }
}
