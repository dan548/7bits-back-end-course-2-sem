package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.core.errorcodes.UsersResponseCode;
import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UsersServiceResponse;
import it.sevenbits.spring_homework.web.service.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SignUpControllerTest {

    private UsersService service;
    private SignUpController controller;

    @Before
    public void setUp() {
        service = mock(UsersService.class);
        controller = new SignUpController(service);
    }

    @Test
    public void testSignUpUser() {
        UsersServiceResponse response = mock(UsersServiceResponse.class);
        SignUpRequest request = mock(SignUpRequest.class);
        when(service.addUser(any(SignUpRequest.class))).thenReturn(response);
        when(response.getCode()).thenReturn(UsersResponseCode.SUCCESS)
                .thenReturn(UsersResponseCode.EXISTS)
                .thenReturn(UsersResponseCode.INVALID);
        assertEquals(HttpStatus.NO_CONTENT, controller.signUpUser(request).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, controller.signUpUser(request).getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, controller.signUpUser(request).getStatusCode());
    }

}
