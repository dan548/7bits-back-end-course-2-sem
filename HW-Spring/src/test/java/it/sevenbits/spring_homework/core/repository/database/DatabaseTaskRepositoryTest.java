package it.sevenbits.spring_homework.core.repository.database;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.repository.tasks.database.DatabaseTaskRepository;
import it.sevenbits.spring_homework.core.service.dategetter.DateGetter;
import it.sevenbits.spring_homework.web.model.taskrequests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.taskrequests.UpdateTaskRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseTaskRepositoryTest {

    private JdbcOperations mockJdbcOperations;
    private DatabaseTaskRepository repository;

    @Before
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
    public void testFindTaskByIdFail() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";

        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), any()))
                .thenThrow(EmptyResultDataAccessException.class);

        Task expectedTask = repository.findTaskById(taskId);

        verify(mockJdbcOperations, times(1)).queryForObject (
                eq("SELECT id, text, status, createdAt, updatedAt FROM task WHERE id = ?"),
                any(RowMapper.class),
                eq(taskId)
        );

        assertNull(expectedTask);
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
        UpdateTaskRequest third = new UpdateTaskRequest(StatusType.DONE.toString(), "abc");

        when(mockJdbcOperations.update(anyString(), Mockito.<String>any(), Mockito.<String>any(), anyString(), eq(taskId))).thenReturn(1);

        int expectedTaskThree = repository.editTaskById(third, taskId);
        int expectedTaskTwo = repository.editTaskById(second, taskId);
        int expectedTaskOne = repository.editTaskById(first, taskId);

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET status = COALESCE(?, status), text = COALESCE(?, text)," +
                        " updatedat = ? WHERE id = ?"),
                eq(third.getStatus()),
                eq(third.getText()),
                anyString(),
                eq(taskId)
        );

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET status = COALESCE(?, status), text = COALESCE(?, text)," +
                        " updatedat = ? WHERE id = ?"),
                eq(second.getStatus()),
                isNull(),
                anyString(),
                eq(taskId)
        );

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET status = COALESCE(?, status), text = COALESCE(?, text)," +
                        " updatedat = ? WHERE id = ?"),
                isNull(),
                eq(first.getText()),
                anyString(),
                eq(taskId)
        );
        assertEquals(1, expectedTaskOne);
        assertEquals(1, expectedTaskTwo);
        assertEquals(1, expectedTaskThree);
    }

    @Test
    public void testEditTaskByIdFail() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest first = new UpdateTaskRequest(null, "ab");
        UpdateTaskRequest second = new UpdateTaskRequest(StatusType.INBOX.toString(), null);
        UpdateTaskRequest third = new UpdateTaskRequest(StatusType.DONE.toString(), "ab");

        when(mockJdbcOperations.update(anyString(), anyString(), anyString(), anyString(), eq(taskId))).thenReturn(0);
        when(mockJdbcOperations.update(anyString(), anyString(), anyString(), eq(taskId))).thenReturn(0);

        int expectedTaskThree = repository.editTaskById(third, taskId);
        int expectedTaskTwo = repository.editTaskById(second, taskId);
        int expectedTaskOne = repository.editTaskById(first, taskId);

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET status = COALESCE(?, status), text = COALESCE(?, text)," +
                        " updatedat = ? WHERE id = ?"),
                eq(third.getStatus()),
                eq(third.getText()),
                anyString(),
                eq(taskId)
        );

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET status = COALESCE(?, status), text = COALESCE(?, text)," +
                        " updatedat = ? WHERE id = ?"),
                eq(second.getStatus()),
                isNull(),
                anyString(),
                eq(taskId)
        );

        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE task SET status = COALESCE(?, status), text = COALESCE(?, text)," +
                        " updatedat = ? WHERE id = ?"),
                isNull(),
                eq(first.getText()),
                anyString(),
                eq(taskId)
        );
        assertEquals(0, expectedTaskOne);
        assertEquals(0, expectedTaskTwo);
        assertEquals(0, expectedTaskThree);
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

    @Test
    public void testGetTaskPage() {
        List<Task> mockTasks = mock(List.class);
        List<Task> mockTasksTwo = mock(List.class);
        when(mockJdbcOperations.query(
                eq("SELECT id, text, status, createdat, updatedat FROM task WHERE status = ? ORDER BY" +
                        " createdat ASC LIMIT ? OFFSET ?"),
                any(RowMapper.class), anyString(), anyInt(), anyInt())).thenReturn(mockTasks);
        when(mockJdbcOperations.query(
                eq("SELECT id, text, status, createdat, updatedat FROM task WHERE status = ? ORDER BY" +
                        " createdat DESC LIMIT ? OFFSET ?"),
                any(RowMapper.class), anyString(), anyInt(), anyInt())).thenReturn(mockTasksTwo);
        assertSame(mockTasks, repository.getTaskPage(StatusType.DONE.toString(), "asc", 1, 20));
        assertSame(mockTasksTwo, repository.getTaskPage(StatusType.DONE.toString(), "desc", 1, 20));
    }

    @Test
    public void testGetSize() {
        when(mockJdbcOperations.queryForObject(eq("SELECT COUNT(*) FROM task WHERE status=?"),
                eq(Integer.class), anyString())).thenReturn(228);
        assertEquals(Integer.valueOf(228), repository.getSize(StatusType.DONE.toString()));
    }
}
