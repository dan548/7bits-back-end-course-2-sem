package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.web.model.users.SignInRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import it.sevenbits.spring_homework.web.security.JwtTokenService;
import it.sevenbits.spring_homework.web.service.LoginService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BodySignInControllerTest {

    private BodySignInController controller;
    private LoginService loginService;
    private JwtTokenService tokenService;

    @Before
    public void setup() {
        loginService = mock(LoginService.class);
        tokenService = mock(JwtTokenService.class);
        controller = new BodySignInController(loginService, tokenService);
    }

    @Test
    public void testCreate() {
        User user = mock(User.class);
        SignInRequest request = mock(SignInRequest.class);
        String token = "abcdef";
        when(loginService.login(any(SignInRequest.class))).thenReturn(user);
        when(tokenService.createToken(any(User.class))).thenReturn(token);
        assertEquals(token, controller.create(request).getToken());
    }

}
