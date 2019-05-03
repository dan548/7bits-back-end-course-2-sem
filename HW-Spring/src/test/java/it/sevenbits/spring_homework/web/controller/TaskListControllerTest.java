package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.response.TaskResponse;
import it.sevenbits.spring_homework.core.service.taskservice.TaskService;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class TaskListControllerTest {

    private TaskService mockService;
    private TaskListController controller;

    @BeforeEach
    public void setup() {
        mockService = mock(TaskService.class);
        controller = new TaskListController(mockService);
    }

    @Test
    public void testGetAllTasks() {
        List<Task> mockTasks = mock(List.class);
        when(mockService.getTasksWithStatus(anyString())).thenReturn(mockTasks);

        List<Task> answer = controller.getAllTasks(StatusType.INBOX.toString());
        verify(mockService, times(1)).getTasksWithStatus(eq(StatusType.INBOX.toString()));
        assertSame(mockTasks, answer);
    }

    @Test
    public void testGetTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        TaskResponse response = new TaskResponse(mockTask);
        when(mockService.findTaskById(anyString())).thenReturn(response);

        ResponseEntity<Task> answer = controller.getTaskById(taskId);
        verify(mockService, times(1)).findTaskById(taskId);
        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertSame(mockTask, answer.getBody());
    }

    @Test
    public void testGetTaskByIdNotFound() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        when(mockService.findTaskById(anyString())).thenReturn(null);

        ResponseEntity<Task> answer = controller.getTaskById(taskId);
        verify(mockService, times(1)).findTaskById(taskId);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
    }

    @Test
    public void testGetTaskByIdBadId() {
        String taskId = "a";
        when(mockService.findTaskById(anyString())).thenReturn(null);

        ResponseEntity<Task> answer = controller.getTaskById(taskId);
        verify(mockService, times(0)).findTaskById(taskId);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
    }

    @Test
    public void testCreate() {
        Task mockTask = mock(Task.class);
        TaskResponse response = new TaskResponse(mockTask);
        AddTaskRequest request = new AddTaskRequest("a");
        when(mockService.create(any(AddTaskRequest.class))).thenReturn(response);

        ResponseEntity<Task> answer = controller.create(request);
        verify(mockService, times(1)).create(eq(request));
        assertEquals(HttpStatus.CREATED, answer.getStatusCode());
    }

    @Test
    public void testCreateBadRequest() {
        Task mockTask = mock(Task.class);
        TaskResponse response = new TaskResponse(mockTask);
        AddTaskRequest request = mock(AddTaskRequest.class);
        when(mockService.create(any(AddTaskRequest.class))).thenReturn(response);

        ResponseEntity<Task> answer = controller.create(request);
        verify(mockService, times(0)).create(any(AddTaskRequest.class));
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
    }

    @Test
    public void testDeleteTask() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        TaskResponse response = new TaskResponse(mockTask);
        when(mockService.removeTaskById(anyString())).thenReturn(response);

        ResponseEntity<Task> answer = controller.deleteTask(taskId);
        verify(mockService, times(1)).removeTaskById(taskId);
        assertEquals(HttpStatus.OK, answer.getStatusCode());
    }

    @Test
    public void testDeleteTaskBadId() {
        String taskId = "a52da-a7846365079d";
        Task mockTask = mock(Task.class);
        TaskResponse response = new TaskResponse(mockTask);
        when(mockService.removeTaskById(anyString())).thenReturn(response);

        ResponseEntity<Task> answer = controller.deleteTask(taskId);
        verify(mockService, times(0)).removeTaskById(taskId);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
    }

    @Test
    public void testUpdateTask() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        TaskResponse response = new TaskResponse(mockTask);
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.DONE.toString(),"a");
        when(mockService.editTaskById(any(UpdateTaskRequest.class), anyString())).thenReturn(response);

        ResponseEntity<Task> answer = controller.updateTask(taskId, request);
        verify(mockService, times(1)).editTaskById(eq(request), eq(taskId));
        assertEquals(HttpStatus.NO_CONTENT, answer.getStatusCode());
    }

    @Test
    public void testUpdateTaskBadId() {
        String taskId = "a55256a8-1247846365079d";
        Task mockTask = mock(Task.class);
        TaskResponse response = new TaskResponse(mockTask);
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.DONE.toString(),"a");
        when(mockService.editTaskById(any(UpdateTaskRequest.class), anyString())).thenReturn(response);

        ResponseEntity<Task> answer = controller.updateTask(taskId, request);
        verify(mockService, times(0)).editTaskById(eq(request), eq(taskId));
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
    }

    @Test
    public void testUpdateTaskBadRequest() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        TaskResponse response = new TaskResponse(TaskResponseErrorCode.BAD_REQUEST);
        UpdateTaskRequest request = mock(UpdateTaskRequest.class);
        when(mockService.editTaskById(any(UpdateTaskRequest.class), anyString())).thenReturn(response);

        ResponseEntity<Task> answer = controller.updateTask(taskId, request);
        verify(mockService, times(1)).editTaskById(eq(request), eq(taskId));
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
    }

}
