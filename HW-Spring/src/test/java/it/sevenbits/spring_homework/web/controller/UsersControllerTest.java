package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.core.errorcodes.UsersResponseCode;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import it.sevenbits.spring_homework.web.model.users.UsersServiceResponse;
import it.sevenbits.spring_homework.web.service.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersControllerTest {

    private UsersService service;
    private UsersController controller;
    private final String id = "a55256a8-1245-4c2c-82da-a7846365079d";
    private final String badId = "a55256ada-a7846365079d";

    @Before
    public void setUp() {
        service = mock(UsersService.class);
        controller = new UsersController(service);
    }

    @Test
    public void testGetAllUsers() {
        List<User> list = mock(List.class);
        when(service.findAll()).thenReturn(list);
        ResponseEntity<List<User>> answer = controller.getAllUsers();
        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertEquals(list, answer.getBody());
    }

    @Test
    public void testGetUserInfo() {
        User user = mock(User.class);
        when(service.findById(eq(id))).thenReturn(user);
        ResponseEntity<User> answer = controller.getUserInfo(id);
        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertEquals(user, answer.getBody());
        answer = controller.getUserInfo(badId);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
    }

    @Test
    public void testUpdateUser() {
        UsersServiceResponse response = mock(UsersServiceResponse.class);
        when(service.editUserById(anyString(), any(UpdateUserRequest.class))).thenReturn(response);
        when(response.getCode()).thenReturn(UsersResponseCode.SUCCESS)
                .thenReturn(UsersResponseCode.NOT_FOUND)
                .thenReturn(UsersResponseCode.INVALID);
        assertEquals(HttpStatus.NO_CONTENT, controller.updateUser("a", mock(UpdateUserRequest.class)).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.updateUser("a", mock(UpdateUserRequest.class)).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, controller.updateUser("a", mock(UpdateUserRequest.class)).getStatusCode());
    }

}
