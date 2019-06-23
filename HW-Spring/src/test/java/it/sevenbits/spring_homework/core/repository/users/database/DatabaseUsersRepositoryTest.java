package it.sevenbits.spring_homework.core.repository.users.database;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DatabaseUsersRepositoryTest {

    private JdbcOperations jdbcOperations;
    private UsersRepository repository;

    @Before
    public void setup() {
        jdbcOperations = mock(JdbcOperations.class);
        repository = new DatabaseUsersRepository(jdbcOperations);
    }

    @Test
    public void testCheckUsername() {
        String username = "Alex";
        String mock = "Daniel";

        when(jdbcOperations.queryForMap(
                eq("SELECT id FROM users u" +
                        " WHERE u.username = ?"),
                AdditionalMatchers.not(eq(username))
        )).thenThrow(IncorrectResultSizeDataAccessException.class);

        assertTrue(repository.checkUsername(username));
        assertFalse(repository.checkUsername(mock));
    }

    @Test
    public void testFindByUserName() {
        String badUsername = "Donald";

        String username = "name";
        String id = "a55256a8-1245-4c2c-82da-a7842365079d";
        String password = "1234";

        Map<String, Object> rawUser;
        rawUser = new HashMap<>();

        rawUser.put("id", id);
        rawUser.put("username", username);
        rawUser.put("password", password);

        when(jdbcOperations.queryForMap(anyString(), eq(username))).thenReturn(rawUser);
        when(jdbcOperations.queryForMap(anyString(), AdditionalMatchers.not(eq(username))))
                .thenThrow(IncorrectResultSizeDataAccessException.class);

        User actualUser = repository.findByUserName(username);
        User badUser = repository.findByUserName(badUsername);

        assertNull(badUser);
        assertEquals(username, actualUser.getUsername());
        assertEquals(id, actualUser.getId());
        assertEquals(password, actualUser.getPassword());
        assertEquals(new ArrayList<>(), actualUser.getAuthorities());
    }

}
