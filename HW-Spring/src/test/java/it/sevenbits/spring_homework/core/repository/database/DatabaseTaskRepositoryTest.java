package it.sevenbits.spring_homework.core.repository.database;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.service.dategetter.DateGetter;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseTaskRepositoryTest {

    private JdbcOperations mockJdbcOperations;
    private DatabaseTaskRepository repository;

    @BeforeEach
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        DateGetter getter = new DateGetter("yyyy-MM-dd'T'HH:mm:ssxxx");
        repository = new DatabaseTaskRepository(mockJdbcOperations, getter);
    }

    @Test
    public void testFindTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);

        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), any())).thenReturn(mockTask);

        Task expectedTask = repository.findTaskById(taskId);

        verify(mockJdbcOperations, times(1)).queryForObject (
                eq("SELECT id, text, status, createdAt, updatedAt FROM task WHERE id = ?"),
                any(RowMapper.class),
                eq(taskId)
        );

        assertEquals(mockTask, expectedTask);
    }

    @Test
    public void testCreate() {
        String title = "title";
        AddTaskRequest request = new AddTaskRequest(title);

        Task task = repository.create(request);

        verify(mockJdbcOperations, times(1)).update(
                eq("INSERT INTO task (id, text, status, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)"),
                anyString(),
                eq(title),
                anyString(),
                anyString(),
                anyString()
        );

        assertEquals(title, task.getText());
    }

    @Test
    public void testRemoveTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);

        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), eq(taskId))).thenReturn(mockTask);
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), AdditionalMatchers.not(eq(taskId)))).thenReturn(null);

        Task expectedTask = repository.removeTaskById(taskId);
        Task expectedNull = repository.removeTaskById("");

        verify(mockJdbcOperations, times(1)).update(
                eq("DELETE FROM task WHERE id=?"),
                eq(taskId)
        );

        assertEquals(mockTask, expectedTask);
        assertNull(expectedNull);
    }

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = mock(List.class);
        when(mockJdbcOperations.query(anyString(), any(RowMapper.class))).thenReturn(tasks);

        List<Task> expectedList = repository.getAllTasks();
        verify(mockJdbcOperations, times(1)).query(
                eq("SELECT id, text, status, createdat, updatedat FROM task"),
                any(RowMapper.class)
        );

        assertSame(expectedList, tasks);
    }

    @Test
    public void testEditTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest first = new UpdateTaskRequest(null, "ab");
        UpdateTaskRequest second = new UpdateTaskRequest(StatusType.INBOX.toString(), null);
        UpdateTaskRequest third = new UpdateTaskRequest(StatusType.DONE.toString(), "ab");

        when(mockJdbcOperations.update(anyString(), anyString(), anyString(), anyString(), eq(taskId))).thenReturn(1);
        when(mockJdbcOperations.update(anyString(), anyString(), anyString(), eq(taskId))).thenReturn(1);

        Task expectedTaskThree = repository.editTaskById(third, taskId);
        Task expectedTaskTwo = repository.editTaskById(second, taskId);
        Task expectedTaskOne = repository.editTaskById(first, taskId);

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET status = ?, text = ?, updatedat = ? WHERE id = ?"),
                eq(third.getStatus()),
                eq(third.getText()),
                anyString(),
                eq(taskId)
        );

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET status = ?, updatedat = ? WHERE id = ?"),
                eq(second.getStatus()),
                anyString(),
                eq(taskId)
        );

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET text = ?, updatedat = ? WHERE id = ?"),
                eq(first.getText()),
                anyString(),
                eq(taskId)
        );
        assertEquals("ab", expectedTaskOne.getText());
        assertNull( expectedTaskOne.getStatus());
        assertEquals(StatusType.INBOX.toString(), expectedTaskTwo.getStatus());
        assertNull( expectedTaskTwo.getText());
        assertEquals("ab", expectedTaskThree.getText());
        assertEquals(StatusType.DONE.toString(), expectedTaskThree.getStatus());
    }

    @Test
    public void testGetTasksWithStatus() {
        String status = StatusType.INBOX.toString();

        List<Task> tasks = mock(List.class);
        when(mockJdbcOperations.query(anyString(), any(RowMapper.class), anyString())).thenReturn(tasks);

        List<Task> expectedList = repository.getTasksWithStatus(status);
        verify(mockJdbcOperations, times(1)).query(
                eq("SELECT id, text, status, createdat, updatedat FROM task WHERE status=?"),
                any(RowMapper.class),
                eq(status)
        );

        assertSame(expectedList, tasks);
    }

}
