package it.sevenbits.spring_homework.core.repository.database;

import it.sevenbits.spring_homework.core.service.dategetter.DateGetter;
import it.sevenbits.spring_homework.core.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    public void testGetAllTasks() {
        List<Task> mockTasks = mock(List.class);

        when(mockJdbcOperations.query(anyString(), any(RowMapper.class))).thenReturn(mockTasks);

        List<Task> expectedList = repository.getAllTasks();
        verify(mockJdbcOperations, times(1)).query(
                eq("SELECT id, text, status, createdat, updatedat FROM task"),
                any(RowMapper.class)
        );

        assertEquals(mockTasks, expectedList);
    }

}
