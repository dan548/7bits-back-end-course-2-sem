package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.GetTasksResponse;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.service_response.TaskResponse;
import it.sevenbits.spring_homework.web.service.taskservice.TaskService;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskListControllerTest {

    @Mock
    private TaskService mockService;

    @InjectMocks
    private TaskListController controller;

    @Test
    public void testGetAllTasks() {
        GetTasksResponse mockResponse = mock(GetTasksResponse.class);
        when(mockService.getTasks(anyString(), anyString(), any(Integer.class),
                any(Integer.class), any(UriComponentsBuilder.class))).thenReturn(mockResponse);

        ResponseEntity<GetTasksResponse> answer = controller.getAllTasks(StatusType.INBOX.toString(), "asc", 1, 10);
        verify(mockService, times(1)).getTasks(eq(StatusType.INBOX.toString()), eq("asc"),
                eq(1), eq(10), any(UriComponentsBuilder.class));
        assertSame(mockResponse, answer.getBody());
    }

    @Test
    public void testGetTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        when(mockService.findTaskById(anyString())).thenReturn(mockTask);

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

        ResponseEntity<Task> answer = controller.getTaskById(taskId);
        verify(mockService, times(0)).findTaskById(taskId);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
    }

    @Test
    public void testCreate() {
        Task mockTask = mock(Task.class);
        AddTaskRequest request = new AddTaskRequest("a");
        when(mockService.create(any(AddTaskRequest.class))).thenReturn(mockTask);

        ResponseEntity<Task> answer = controller.create(request);
        verify(mockService, times(1)).create(eq(request));
        assertEquals(HttpStatus.CREATED, answer.getStatusCode());
    }

    @Test
    public void testCreateBadRequest() {
        AddTaskRequest request = mock(AddTaskRequest.class);
        ResponseEntity<Task> answer = controller.create(request);
        verify(mockService, never()).create(any(AddTaskRequest.class));
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
    }

    @Test
    public void testDeleteTask() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        when(mockService.removeTaskById(anyString())).thenReturn(mockTask);

        ResponseEntity<Task> answer = controller.deleteTask(taskId);
        verify(mockService, times(1)).removeTaskById(taskId);
        assertEquals(HttpStatus.OK, answer.getStatusCode());
    }

    @Test
    public void testDeleteTaskBadId() {
        String taskId = "a52da-a7846365079d";
        ResponseEntity<Task> answer = controller.deleteTask(taskId);
        verify(mockService, never()).removeTaskById(taskId);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
    }

    @Test
    public void testUpdateTask() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        TaskResponse mockResponse = mock(TaskResponse.class);
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.DONE.toString(),"a");
        when(mockService.editTaskById(any(UpdateTaskRequest.class), anyString())).thenReturn(mockResponse);

        ResponseEntity<Task> answer = controller.updateTask(taskId, request);
        verify(mockService, times(1)).editTaskById(eq(request), eq(taskId));
        assertEquals(HttpStatus.NO_CONTENT, answer.getStatusCode());
    }

    @Test
    public void testUpdateTaskBadId() {
        String taskId = "a55256a8-1247846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.DONE.toString(),"a");
        ResponseEntity<Task> answer = controller.updateTask(taskId, request);
        verify(mockService, never()).editTaskById(eq(request), eq(taskId));
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
