package it.sevenbits.spring_homework.core.errorcodes;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTaskResponseErrorCode {

    @Test
    public void testGetters() {
        assertEquals("Invalid request.", TaskResponseErrorCode.BAD_REQUEST.getErrorCode());
        assertEquals(ResponseEntity.badRequest().build(), TaskResponseErrorCode.BAD_REQUEST.getEntity());
    }

}
