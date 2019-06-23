package it.sevenbits.spring_homework.core.repository.tasks.database;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.service.dategetter.DateGetter;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.repository.tasks.TaskRepository;
import it.sevenbits.spring_homework.web.model.taskrequests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.taskrequests.UpdateTaskRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * PostgreSQL task repository implementation.
 */
public class DatabaseTaskRepository implements TaskRepository {

    private final JdbcOperations jdbcOperations;
    private final DateGetter dateGetter;

    private String getId(final User user) {
        String ownerId = user.getId();
        if (ownerId == null) {
            ownerId = jdbcOperations.queryForObject("SELECT id FROM users u" +
                            " WHERE u.enabled = true AND u.username = ?",
                    (rs, i) -> rs.getString("id"), user.getUsername());
        }
        return ownerId;
    }

    /**
     * Constructs a new repository.
     * @param jdbcOperations current jdbc settings
     * @param dateGetter date getter
     */
    public DatabaseTaskRepository(final JdbcOperations jdbcOperations, final DateGetter dateGetter) {
        this.jdbcOperations = jdbcOperations;
        this.dateGetter = dateGetter;
    }

    private static Task mapRow(final ResultSet resultSet, final int i) throws SQLException {
        String id = resultSet.getString("id");
        String text = resultSet.getString("text");
        String status = resultSet.getString("status");
        String createdAt = resultSet.getString("createdAt");
        String updatedAt = resultSet.getString("updatedAt");
        String owner = resultSet.getString("owner");
        return new Task(id, text, status, createdAt, updatedAt, owner);
    }

    @Override
    public Task findTaskById(final String givenId) {
        User user = new User(SecurityContextHolder.getContext().getAuthentication());
        String ownerId = getId(user);
        try {
            return jdbcOperations.queryForObject(
                    "SELECT id, text, status, createdAt, updatedAt, owner FROM task WHERE id = ? AND owner = ?",
                    DatabaseTaskRepository::mapRow, givenId, ownerId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Task create(final AddTaskRequest request) {
        String date = dateGetter.getDate();
        User user = new User(SecurityContextHolder.getContext().getAuthentication());
        String ownerId = getId(user);
        Task newTask = new Task(UUID.randomUUID().toString(), request.getText(), StatusType.INBOX.toString(),
                date, date, ownerId);
        jdbcOperations.update("INSERT INTO task (id, text, status, createdAt, updatedAt, owner)" +
                        " VALUES (?, ?, ?, ?, ?, ?)",
                newTask.getId(), newTask.getText(), newTask.getStatus(),
                newTask.getCreatedAt(), newTask.getUpdatedAt(), newTask.getOwner());

        return newTask;
    }

    @Override
    public Task removeTaskById(final String id) {
        Task task = findTaskById(id);
        if (task != null) {
            jdbcOperations.update("DELETE FROM task WHERE id=?", id);
        }
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        User user = new User(SecurityContextHolder.getContext().getAuthentication());
        String ownerId = getId(user);
        return jdbcOperations.query(
                "SELECT id, text, status, createdat, updatedat, owner FROM task WHERE owner = ?",
                DatabaseTaskRepository::mapRow, ownerId);
    }

    @Override
    public int editTaskById(final UpdateTaskRequest request, final String id) {
        String date = dateGetter.getDate();
        User user = new User(SecurityContextHolder.getContext().getAuthentication());
        String ownerId = getId(user);
        return jdbcOperations.update("UPDATE task SET status = COALESCE(?, status), text = COALESCE(?, text), " +
                        "updatedat = ? WHERE id = ? AND owner = ?", request.getStatus(),
                request.getText(), date, id, ownerId);
    }

    @Override
    public List<Task> getTasksWithStatus(final String status) {
        User user = new User(SecurityContextHolder.getContext().getAuthentication());
        String ownerId = getId(user);
        return jdbcOperations.query(
                "SELECT id, text, status, createdat, updatedat, owner FROM task WHERE status=? AND owner = ?",
                DatabaseTaskRepository::mapRow, status, ownerId);
    }

    @Override
    public List<Task> getTaskPage(final String status, final String order, final int page, final int size) {
        User user = new User(SecurityContextHolder.getContext().getAuthentication());
        String ownerId = getId(user);
        if ("asc".equals(order)) {
            return jdbcOperations.query(
                    "SELECT id, text, status, createdat, updatedat, owner FROM task WHERE status = ? AND owner = ? " +
                            "ORDER BY createdat ASC LIMIT ? OFFSET ?",
                    DatabaseTaskRepository::mapRow, status, ownerId, size, (page - 1) * size);
        } else {
            return jdbcOperations.query(
                    "SELECT id, text, status, createdat, updatedat, owner FROM task WHERE status = ? AND owner = ? " +
                            "ORDER BY createdat DESC LIMIT ? OFFSET ?",
                    DatabaseTaskRepository::mapRow, status, ownerId, size, (page - 1) * size);
        }
    }

    @Override
    public Integer getSize(final String status) {
        User user = new User(SecurityContextHolder.getContext().getAuthentication());
        String ownerId = getId(user);
        return jdbcOperations.queryForObject("SELECT COUNT(*) FROM task WHERE status=? AND owner = ?",
                Integer.class, status, ownerId);
    }
}
