package it.sevenbits.spring_homework.core.model;

import it.sevenbits.spring_homework.config.constant.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTask {

    private Task task;

    @BeforeEach
    public void setup() {
        task = new Task("a", "b", "c", "d", "e");
    }

    @Test
    public void testSetStatus() {
        task.setStatus(StatusType.DONE.toString());
        assertEquals(StatusType.DONE.toString(), task.getStatus());
    }

    @Test
    public void testSetText() {
        task.setStatus("b");
        assertEquals("b", task.getText());
    }

    @Test
    public void testSetUpdatedAt() {
        task.setUpdatedAt("");
        assertEquals("", task.getUpdatedAt());
    }
}
