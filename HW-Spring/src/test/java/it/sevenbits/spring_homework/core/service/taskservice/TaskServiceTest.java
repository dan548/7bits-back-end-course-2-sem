package it.sevenbits.spring_homework.core.service.taskservice;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.response.TaskResponse;
import it.sevenbits.spring_homework.core.repository.TaskRepository;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository mockRepository;
    private TaskService service;

    @BeforeEach
    public void setup() {
        mockRepository = mock(TaskRepository.class);
        service = new TaskService(mockRepository);
    }

    @Test
    public void testGetTasksWithStatus() {
        List<Task> mockListTasks = mock(List.class);
        when(mockRepository.getTasksWithStatus(anyString())).thenReturn(mockListTasks);

        List<Task> result = service.getTasksWithStatus(StatusType.DONE.toString());
        verify(mockRepository, times(1)).getTasksWithStatus(eq(StatusType.DONE.toString()));
        assertSame(mockListTasks, result);
    }

    @Test
    public void testFindTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        when(mockRepository.findTaskById(taskId)).thenReturn(mockTask);

        TaskResponse result = service.findTaskById(taskId);
        verify(mockRepository, times(1)).findTaskById(taskId);
        assertSame(mockTask, result.getTask());
    }

    @Test
    public void testFindTaskByIdNotFound() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        when(mockRepository.findTaskById(taskId)).thenReturn(null);

        TaskResponse result = service.findTaskById(taskId);
        verify(mockRepository, times(1)).findTaskById(taskId);
        assertEquals(TaskResponseErrorCode.NOT_FOUND, result.getCode());
    }

    @Test
    public void testCreate() {
        Task mockTask = mock(Task.class);
        AddTaskRequest request = mock(AddTaskRequest.class);
        when(mockRepository.create(request)).thenReturn(mockTask);

        TaskResponse result = service.create(request);
        verify(mockRepository, times(1)).create(request);
        assertEquals(mockTask, result.getTask());
    }

    @Test
    public void testRemoveTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        when(mockRepository.removeTaskById(taskId)).thenReturn(mockTask);

        TaskResponse result = service.removeTaskById(taskId);
        verify(mockRepository, times(1)).removeTaskById(taskId);
        assertSame(mockTask, result.getTask());
    }

    @Test
    public void testRemoveTaskByIdBadId() {
        String taskId = "a55256a8-1245-4c2c-82da-a7";
        when(mockRepository.removeTaskById(taskId)).thenReturn(mock(Task.class));

        TaskResponse result = service.removeTaskById(taskId);
        verify(mockRepository, times(0)).removeTaskById(taskId);
        assertNull(result);
    }

    @Test
    public void testEditTaskByIdNotFoundWithStatus() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.INBOX.toString(), null);
        when(mockRepository.editTaskById(request, taskId)).thenReturn(null);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(1)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.NOT_FOUND, result.getCode());
    }

    @Test
    public void testEditTaskByIdNotFoundWithText() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest(null, "a");
        when(mockRepository.editTaskById(request, taskId)).thenReturn(null);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(1)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.NOT_FOUND, result.getCode());
    }

    @Test
    public void testEditTaskByIdBadStatus() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest("das", "a");
        when(mockRepository.editTaskById(request, taskId)).thenReturn(mock(Task.class));

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(0)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.BAD_STATUS, result.getCode());
    }

    @Test
    public void testEditTaskByIdBadRequest() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        UpdateTaskRequest request = mock(UpdateTaskRequest.class);
        when(mockRepository.editTaskById(request, taskId)).thenReturn(mockTask);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(0)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.BAD_REQUEST, result.getCode());
    }

    @Test
    public void testEditTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        UpdateTaskRequest request = new UpdateTaskRequest(null, "title");
        when(mockRepository.editTaskById(request, taskId)).thenReturn(mockTask);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(1)).editTaskById(request, taskId);
        assertSame(mockTask, result.getTask());
    }

    @Test
    public void testEditTaskByIdBadId() {
        String taskId = "a55256a8-1245-4c2365079d";
        Task mockTask = mock(Task.class);
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.DONE.toString(), "title");
        when(mockRepository.editTaskById(request, taskId)).thenReturn(mockTask);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(0)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.NOT_FOUND, result.getCode());
    }

    @Test
    public void testEditTaskByIdSecond() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.INBOX.toString(), "title");
        when(mockRepository.editTaskById(request, taskId)).thenReturn(mockTask);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(1)).editTaskById(request, taskId);
        assertSame(mockTask, result.getTask());
    }
}
