package it.sevenbits.spring_homework.core.repository.users.database;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.*;

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

    @Test
    public void testFindById() {
        String badId = "Donald";

        String id = "a55256a8-1245-4c2c-82da-a7842365079d";
        String password = "1234";

        Map<String, Object> rawUser;
        rawUser = new HashMap<>();

        rawUser.put("id", id);
        rawUser.put("password", password);

        when(jdbcOperations.queryForMap(anyString(), eq(id))).thenReturn(rawUser);
        when(jdbcOperations.queryForMap(anyString(), AdditionalMatchers.not(eq(id))))
                .thenThrow(IncorrectResultSizeDataAccessException.class);

        User actualUser = repository.findById(id);
        User badUser = repository.findById(badId);

        assertNull(badUser);
        assertNull(actualUser.getUsername());
        assertEquals(id, actualUser.getId());
        assertEquals(password, actualUser.getPassword());
        assertEquals(new ArrayList<>(), actualUser.getAuthorities());
    }

    @Test
    public void testFindAll() {
        List<Map<String, Object>> userInfo = new ArrayList<>();

        Map<String, Object> userOne = new HashMap<>();
        userOne.put("id", "123");
        userOne.put("username", "a");
        userOne.put("authority", "ADMIN");

        userInfo.add(userOne);

        Map<String, Object> userTwo = new HashMap<>();
        userTwo.put("id", "123");
        userTwo.put("username", "a");
        userTwo.put("authority", "USER");

        userInfo.add(userTwo);

        Map<String, Object> userThree = new HashMap<>();
        userThree.put("id", "456");
        userThree.put("username", "b");
        userThree.put("authority", "USER");

        userInfo.add(userThree);

        when(jdbcOperations.queryForList(anyString())).thenReturn(userInfo);

        List<String> listOne = new ArrayList<>();
        listOne.add("ADMIN");
        listOne.add("USER");
        User realOne = new User("123", "a", listOne);

        List<String> listTwo = new ArrayList<>();
        listTwo.add("USER");
        User realTwo = new User("456", "b", listTwo);

        List<User> expectedList = new ArrayList<>();
        expectedList.add(realOne);
        expectedList.add(realTwo);

        assertEquals(expectedList, repository.findAll());

        verify(jdbcOperations, times(1)).queryForList(
                eq("SELECT u.id, u.username, a.authority FROM authorities a, users u" +
                        " WHERE u.id = a.id AND u.enabled = true")
        );
    }

    @Test
    public void testEditUserById() {
        UpdateUserRequest mockRequest = mock(UpdateUserRequest.class);
        String id = "a55256a8-1245-4c2c-82da-a7842365079d";
        List<String> mockList = mock(List.class);
        Iterator<String> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(iterator.next()).thenReturn("a").thenReturn("b");
        when(mockRequest.getAuthorities()).thenReturn(mockList);
        when(mockList.size()).thenReturn(2);
        when(mockList.iterator()).thenReturn(iterator);
        when(mockRequest.getStatus()).thenReturn(false);
        when(jdbcOperations.update(eq("UPDATE users SET enabled = COALESCE(?, enabled) WHERE id = ?"),
                eq(false), anyString())).thenReturn(1);
        when(jdbcOperations.update(eq("DELETE FROM authorities WHERE id = ?"), eq(id)))
                .thenReturn(2);
        assertEquals(1, repository.editUserById(id, mockRequest));
        assertEquals(0, repository.editUserById("a", mockRequest));
        verify(jdbcOperations, times(2))
                .update(eq("INSERT INTO authorities (id, authority) VALUES (?, ?)"),
                eq(id), anyString());
    }

    @Test
    public void testAddUser() {
        SignUpRequest mockRequest = mock(SignUpRequest.class);
        when(mockRequest.getUsername()).thenReturn("a");
        when(mockRequest.getPassword()).thenReturn("b");
        when(jdbcOperations.update(anyString(), anyString(), anyString())).thenReturn(1);

        assertEquals(1, repository.addUser(mockRequest));
    }
}
