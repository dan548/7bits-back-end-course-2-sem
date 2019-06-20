package it.sevenbits.spring_homework.core.repository.tasks.database;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.service.dategetter.DateGetter;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.repository.tasks.TaskRepository;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

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
        return new Task(id, text, status, createdAt, updatedAt);
    }

    @Override
    public Task findTaskById(final String givenId) {
        try {
            return jdbcOperations.queryForObject(
                    "SELECT id, text, status, createdAt, updatedAt FROM task WHERE id = ?",
                    DatabaseTaskRepository::mapRow, givenId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Task create(final AddTaskRequest request) {
        String date = dateGetter.getDate();
        Task newTask = new Task(UUID.randomUUID().toString(), request.getText(), StatusType.INBOX.toString(), date, date);
        jdbcOperations.update("INSERT INTO task (id, text, status, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)",
                newTask.getId(), newTask.getText(), newTask.getStatus(), newTask.getCreatedAt(), newTask.getUpdatedAt());

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
        return jdbcOperations.query(
                "SELECT id, text, status, createdat, updatedat FROM task",
                DatabaseTaskRepository::mapRow);
    }

    @Override
    public int editTaskById(final UpdateTaskRequest request, final String id) {
        String date = dateGetter.getDate();
        return jdbcOperations.update("UPDATE task SET status = COALESCE(?, status), text = COALESCE(?, text), " +
                        "updatedat = ? WHERE id = ?", request.getStatus(), request.getText(), date, id);
    }

    @Override
    public List<Task> getTasksWithStatus(final String status) {
        return jdbcOperations.query(
                "SELECT id, text, status, createdat, updatedat FROM task WHERE status=?",
                DatabaseTaskRepository::mapRow, status);
    }

    @Override
    public List<Task> getTaskPage(final String status, final String order, final int page, final int size) {
        if ("asc".equals(order)) {
            return jdbcOperations.query(
                    "SELECT id, text, status, createdat, updatedat FROM task WHERE status = ? ORDER BY createdat ASC LIMIT ? OFFSET ?",
                    DatabaseTaskRepository::mapRow, status, size, (page - 1) * size);
        } else {
            return jdbcOperations.query(
                    "SELECT id, text, status, createdat, updatedat FROM task WHERE status = ? ORDER BY createdat DESC LIMIT ? OFFSET ?",
                    DatabaseTaskRepository::mapRow, status, size, (page - 1) * size);
        }
    }

    @Override
    public Integer getSize(final String status) {
        return jdbcOperations.queryForObject("SELECT COUNT(*) FROM task WHERE status=?", Integer.class, status);
    }
}
