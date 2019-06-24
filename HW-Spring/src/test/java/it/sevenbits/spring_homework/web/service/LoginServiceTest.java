package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.SignInRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    private UsersRepository mockRepository;
    private PasswordEncoder mockEncoder;
    private LoginService service;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        mockRepository = mock(UsersRepository.class);
        mockEncoder = mock(PasswordEncoder.class);
        service = new LoginService(mockRepository, mockEncoder);
    }

    @Test
    public void testLoginNotFound() {
        SignInRequest request = mock(SignInRequest.class);

        when(request.getLogin()).thenReturn("admin");
        when(request.getPassword()).thenReturn("123");

        thrown.expect(LoginFailedException.class);
        thrown.expectMessage("User 'admin' not found");

        service.login(request);

        thrown = ExpectedException.none();
    }

    @Test
    public void testLoginBadPassword() {
        SignInRequest request = mock(SignInRequest.class);

        when(request.getLogin()).thenReturn("admin");
        when(request.getPassword()).thenReturn("123");
        when(mockRepository.findByUserName(anyString())).thenReturn(mock(User.class));
        when(mockEncoder.matches(anyString(), anyString())).thenReturn(false);

        thrown.expect(LoginFailedException.class);
        thrown.expectMessage("Wrong password");

        service.login(request);

        thrown = ExpectedException.none();
    }

    @Test
    public void testLoginOK() {
        SignInRequest request = mock(SignInRequest.class);
        User user = mock(User.class);

        when(request.getLogin()).thenReturn("admin");
        when(request.getPassword()).thenReturn("123");
        when(user.getPassword()).thenReturn("a");
        when(user.getUsername()).thenReturn("admin");
        when(mockRepository.findByUserName(anyString())).thenReturn(user);
        when(mockEncoder.matches(eq("123"), anyString())).thenReturn(true);

        assertEquals("admin", service.login(request).getUsername());
    }

}
