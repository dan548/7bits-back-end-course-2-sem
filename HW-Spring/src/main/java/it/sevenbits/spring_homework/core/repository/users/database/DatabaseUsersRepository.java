package it.sevenbits.spring_homework.core.repository.users.database;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.*;

public class DatabaseUsersRepository implements UsersRepository {

    private final JdbcOperations jdbcOperations;
    private final String AUTHORITY = "authority";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    public DatabaseUsersRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public boolean checkUsername(String username) {
        try {
            jdbcOperations.queryForMap(
                    "SELECT id FROM users u" +
                            " WHERE u.username = ?",
                    username
            );
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    public User findByUserName(String username) {
        Map<String, Object> rawUser;

        try {
            rawUser = jdbcOperations.queryForMap(
                    "SELECT id, username, password FROM users u" +
                            " WHERE u.enabled = true AND u.username = ?",
                    username
            );
        } catch (IncorrectResultSizeDataAccessException e){
            return null;
        }

        List<String> authorities = new ArrayList<>();
        jdbcOperations.query(
                "SELECT u.id, u.username, a.authority FROM authorities a, users u" +
                        " WHERE u.username = ? AND a.id = u.id",
                resultSet -> {
                    String authority = resultSet.getString(AUTHORITY);
                    authorities.add(authority);
                },
                username
        );
        String id = String.valueOf(rawUser.get("id"));
        String password = String.valueOf(rawUser.get(PASSWORD));
        return new User(id, username, password, authorities);
    }

    @Override
    public User findById(String id) {
        Map<String, Object> rawUser;

        try {
            rawUser = jdbcOperations.queryForMap(
                    "SELECT id, username, password FROM users u" +
                            " WHERE enabled = true AND id = ?",
                    id
            );
        } catch (IncorrectResultSizeDataAccessException e){
            return null;
        }

        List<String> authorities = new ArrayList<>();
        final String[] name = {null};

        jdbcOperations.query(
                "SELECT u.id, a.authority, u.username FROM authorities a, users u" +
                        " WHERE u.id = ? AND u.id = a.id",
                resultSet -> {
                    if (name[0] == null) {
                        name[0] = resultSet.getString(USERNAME);
                    }
                    String authority = resultSet.getString(AUTHORITY);
                    authorities.add(authority);
                },
                id
        );

        String password = String.valueOf(rawUser.get(PASSWORD));
        return new User(id, name[0], password, authorities);
    }

    public List<User> findAll() {
        HashMap<String, User> users = new HashMap<>();

        for (Map<String, Object> row : jdbcOperations.queryForList(
                "SELECT u.id, u.username, a.authority FROM authorities a, users u" +
                        " WHERE u.id = a.id AND u.enabled = true")) {

            String id = String.valueOf(row.get("id"));
            String username = String.valueOf(row.get(USERNAME));
            String newRole = String.valueOf(row.get(AUTHORITY));
            User user = users.computeIfAbsent(id, someId -> new User(someId, username, new ArrayList<>()));
            List<String> roles = user.getAuthorities();
            roles.add(newRole);

        }

        return new ArrayList<>(users.values());
    }

    @Override
    public int editUserById(final String id, final UpdateUserRequest request) {
        int rowStatus = jdbcOperations.update("UPDATE users SET enabled = COALESCE(?, enabled) WHERE id = ?",
                request.getStatus(), id);
        List<String> list = request.getAuthorities();
        if (list != null && list.size() != 0) {
            int auth = jdbcOperations.update("DELETE FROM authorities WHERE id = ?", id);
            if (auth == 0) {
                return 0;
            }
            for (String authority : list) {
                jdbcOperations.update("INSERT INTO authorities (id, authority) VALUES (?, ?)", id, authority);
            }
        }
        return rowStatus;
    }

    @Override
    public int addUser(final SignUpRequest request) {
        String id = UUID.randomUUID().toString();
        jdbcOperations.update("INSERT INTO users (id, username, password, enabled) VALUES (?, ?, ?, TRUE)",
                id, request.getUsername(), request.getPassword());
        return jdbcOperations.update("INSERT INTO authorities (id, authority) VALUES (?, ?)",
                id, "USER");
    }
}
