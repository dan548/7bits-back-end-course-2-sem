package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.core.errorcodes.UsersResponseCode;
import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTest {

    private UsersRepository mockRepository;
    private PasswordEncoder mockEncoder;
    private UsersService service;
    private final String id = "a55256a8-1245-4c2c-82da-a7846365079d";
    private final String badId = "a556a8-1245-4c2c-82da-a7846365079d";

    @Before
    public void setup() {
        mockRepository = mock(UsersRepository.class);
        mockEncoder = mock(PasswordEncoder.class);
        service = new UsersService(mockRepository, mockEncoder);
    }

    @Test
    public void testEditUserById() {
        UpdateUserRequest request = mock(UpdateUserRequest.class);

        when(request.getStatus()).thenReturn(true);        when(mockRepository.editUserById(anyString(), any(UpdateUserRequest.class)))
                .thenReturn(1).thenReturn(0);

        assertEquals(UsersResponseCode.SUCCESS, service.editUserById(id, request).getCode());
        assertEquals(UsersResponseCode.NOT_FOUND, service.editUserById(id, request).getCode());
        assertEquals(UsersResponseCode.INVALID, service.editUserById(badId, request).getCode());
        assertEquals(UsersResponseCode.INVALID, service.editUserById(id, null).getCode());

        verify(mockRepository, times(2)).editUserById(id, request);
    }

    @Test
    public void testAddUser() {
        assertEquals(UsersResponseCode.INVALID, service.addUser(null).getCode());

        verify(mockRepository, never())
                .addUser(eq(new SignUpRequest("user", "code")));
    }

    @Test
    public void testFindById() {
        User user = mock(User.class);
        when(mockRepository.findById(anyString())).thenReturn(user);
        assertEquals(user, service.findById("a"));
    }

    @Test
    public void testFindAll() {
        List<User> list = mock(List.class);
        when(mockRepository.findAll()).thenReturn(list);
        assertEquals(list, service.findAll());
    }

}
