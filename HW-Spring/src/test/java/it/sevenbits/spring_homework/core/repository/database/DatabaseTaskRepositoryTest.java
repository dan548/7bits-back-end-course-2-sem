package it.sevenbits.spring_homework.core.repository.database;

import it.sevenbits.spring_homework.core.dategetter.DateGetter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcOperations;

import static org.mockito.Mockito.mock;

public class DatabaseTaskRepositoryTest {

    private JdbcOperations mockJdbcOperations;
    private DatabaseTaskRepository repository;

    @BeforeAll
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        DateGetter getter = new DateGetter("yyyy-MM-dd'T'HH:mm:ssxxx");
        repository = new DatabaseTaskRepository(mockJdbcOperations, getter);
    }

    @Test
    public void test() {

    }

}
