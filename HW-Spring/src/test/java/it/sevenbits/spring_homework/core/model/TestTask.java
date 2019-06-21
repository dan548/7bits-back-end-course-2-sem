package it.sevenbits.spring_homework.core.model;

import it.sevenbits.spring_homework.config.constant.StatusType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestTask {

    private Task task;

    @Before
    public void setup() {
        task = new Task("a", "b", "c", "d", "e", "f");
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
