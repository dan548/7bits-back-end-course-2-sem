package it.sevenbits.spring_homework.core.repository.users.database;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * User database repository
 */
public class DatabaseUsersRepository implements UsersRepository {

    private final JdbcOperations jdbcOperations;

    /**
     * Constructs repository
     * @param jdbcOperations JDBC
     */
    public DatabaseUsersRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public boolean checkUsername(final String username) {
        try {
            String id = jdbcOperations.queryForObject(
                    "SELECT id FROM users u WHERE u.username = ?",
                    String.class, username
            );
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    /**
     * Gets user by name
     * @param username login
     * @return user
     */
    public User findByUserName(final String username) {
        Map<String, Object> rawUser;

        try {
            rawUser = jdbcOperations.queryForMap(
                    "SELECT id, username, password FROM users u" +
                            " WHERE u.enabled = true AND u.username = ?",
                    username
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        List<String> authorities = new ArrayList<>();
        jdbcOperations.query(
                "SELECT a.authority FROM authorities a, users u" +
                        " WHERE u.username = ? AND a.id = u.id",
                resultSet -> {
                    String authority = resultSet.getString("authority");
                    authorities.add(authority);
                },
                username
        );
        String id = String.valueOf(rawUser.get("id"));
        String password = String.valueOf(rawUser.get("password"));
        return new User(id, username, password, authorities);
    }

    @Override
    public User findById(final String id) {
        Map<String, Object> rawUser;

        try {
            rawUser = jdbcOperations.queryForMap(
                    "SELECT id, username, password FROM users u" +
                            " WHERE enabled = true AND id = ?",
                    id
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        List<String> authorities = new ArrayList<>();
        final String[] name = {null};

        jdbcOperations.query(
                "SELECT u.id, a.authority, u.username FROM authorities a, users u" +
                        " WHERE u.id = ? AND u.id = a.id",
                resultSet -> {
                    if (name[0] == null) {
                        name[0] = resultSet.getString("username");
                    }
                    String authority = resultSet.getString("authority");
                    authorities.add(authority);
                },
                id
        );

        String password = String.valueOf(rawUser.get("password"));
        return new User(id, name[0], password, authorities);
    }

    /**
     * Gets list of all users
     * @return list of users
     */
    public List<User> findAll() {
        HashMap<String, User> users = new HashMap<>();

        for (Map<String, Object> row : jdbcOperations.queryForList(
                "SELECT u.id, u.username, a.authority FROM authorities a, users u" +
                        " WHERE u.id = a.id AND u.enabled = true")) {

            String id = String.valueOf(row.get("id"));
            String username = String.valueOf(row.get("username"));
            String newRole = String.valueOf(row.get("authority"));
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
