package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration
@WithMockUser(username = "admin", password = "petooh", authorities = {"ADMIN", "USER"})
public class WhoAmIControllerTest {

    private UsersRepository repository;
    private WhoAmIController controller;

    @Before
    public void setUp() {
        repository = mock(UsersRepository.class);
        controller = new WhoAmIController(repository);
    }

    @Test
    public void testGet() {
        User mockUser = mock(User.class);
        when(repository.findByUserName(anyString())).thenReturn(mockUser);
        when(repository.findById(anyString())).thenReturn(mockUser);
        ResponseEntity<User> actual = controller.get();
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(mockUser, actual.getBody());
    }

}
