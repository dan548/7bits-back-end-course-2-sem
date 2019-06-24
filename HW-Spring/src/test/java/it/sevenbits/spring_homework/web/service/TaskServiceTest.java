package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.GetTasksResponse;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.TasksPaginationMeta;
import it.sevenbits.spring_homework.core.model.service_response.TaskResponse;
import it.sevenbits.spring_homework.core.repository.tasks.TaskRepository;
import it.sevenbits.spring_homework.web.model.taskrequests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.taskrequests.UpdateTaskRequest;
import it.sevenbits.spring_homework.web.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    private TaskRepository mockRepository;
    private TaskService service;

    @Before
    public void setup() {
        mockRepository = mock(TaskRepository.class);
        service = new TaskService(mockRepository, 10, 50);
    }

    @Test
    public void testGetTasks() {
        List<Task> mockList = mock(List.class);
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);

        when(builder.queryParam(anyString(), any())).thenReturn(builder);
        when(builder.replaceQueryParam(anyString(), any())).thenReturn(builder);
        when(builder.toUriString()).thenReturn("next").thenReturn("prev").thenReturn("first").thenReturn("last");

        TasksPaginationMeta meta = new TasksPaginationMeta(100, 3, 20, "next",
                "prev", "first", "last");
        GetTasksResponse mockResponse = new GetTasksResponse(meta, mockList);

        when(mockRepository.getTaskPage(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockList);
        when(mockRepository.getSize(anyString())).thenReturn(100);

        String order = "desc";

        GetTasksResponse result = service.getTasks(StatusType.DONE.toString(), order, 3, 20, builder);
        verify(mockRepository, times(1)).getTaskPage(eq(StatusType.DONE.toString()),
                eq(order), eq(3), eq(20));
        assertEquals(mockResponse, result);
    }

    @Test
    public void testFindTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        when(mockRepository.findTaskById(taskId)).thenReturn(mockTask);

        Task result = service.findTaskById(taskId);
        verify(mockRepository, times(1)).findTaskById(taskId);
        assertSame(mockTask, result);
    }

    @Test
    public void testFindTaskByIdNotFound() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        when(mockRepository.findTaskById(taskId)).thenReturn(null);

        Task result = service.findTaskById(taskId);
        verify(mockRepository, times(1)).findTaskById(taskId);
        assertNull(result);
    }

    @Test
    public void testCreate() {
        Task mockTask = mock(Task.class);
        AddTaskRequest request = mock(AddTaskRequest.class);
        when(mockRepository.create(request)).thenReturn(mockTask);

        Task result = service.create(request);
        verify(mockRepository, times(1)).create(request);
        assertEquals(mockTask, result);
    }

    @Test
    public void testRemoveTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        Task mockTask = mock(Task.class);
        when(mockRepository.removeTaskById(taskId)).thenReturn(mockTask);

        Task result = service.removeTaskById(taskId);
        verify(mockRepository, times(1)).removeTaskById(taskId);
        assertSame(mockTask, result);
    }

    @Test
    public void testRemoveTaskByIdBadId() {
        String taskId = "a55256a8-1245-4c2c-82da-a7";

        Task result = service.removeTaskById(taskId);
        verify(mockRepository, times(0)).removeTaskById(taskId);
        assertNull(result);
    }

    @Test
    public void testEditTaskByIdNotFoundWithStatus() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.INBOX.toString(), null);
        when(mockRepository.editTaskById(request, taskId)).thenReturn(0);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(1)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.NOT_FOUND, result.getCode());
    }

    @Test
    public void testEditTaskByIdNotFoundWithText() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest(null, "a");
        when(mockRepository.editTaskById(request, taskId)).thenReturn(0);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(1)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.NOT_FOUND, result.getCode());
    }

    @Test
    public void testEditTaskByIdBadStatus() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest("das", "a");

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(0)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.BAD_STATUS, result.getCode());
    }

    @Test
    public void testEditTaskByIdBadRequest() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = mock(UpdateTaskRequest.class);

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(0)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.BAD_REQUEST, result.getCode());
    }

    @Test
    public void testEditTaskById() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest(null, "title");
        when(mockRepository.editTaskById(request, taskId)).thenReturn(1);

        service.editTaskById(request, taskId);
        verify(mockRepository, times(1)).editTaskById(request, taskId);
    }

    @Test
    public void testEditTaskByIdBadId() {
        String taskId = "a55256a8-1245-4c2365079d";
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.DONE.toString(), "title");

        TaskResponse result = service.editTaskById(request, taskId);
        verify(mockRepository, times(0)).editTaskById(request, taskId);
        assertEquals(TaskResponseErrorCode.NOT_FOUND, result.getCode());
    }

    @Test
    public void testEditTaskByIdSecond() {
        String taskId = "a55256a8-1245-4c2c-82da-a7846365079d";
        UpdateTaskRequest request = new UpdateTaskRequest(StatusType.INBOX.toString(), "title");
        when(mockRepository.editTaskById(request, taskId)).thenReturn(1);

        service.editTaskById(request, taskId);
        verify(mockRepository, times(1)).editTaskById(request, taskId);
    }

    @Test
    public void testGetTasksNoPageLow() {
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);

        when(builder.queryParam(anyString(), any())).thenReturn(builder);
        when(builder.replaceQueryParam(anyString(), any())).thenReturn(builder);
        when(builder.toUriString()).thenReturn("next").thenReturn("prev").thenReturn("first").thenReturn("last");

        when(mockRepository.getSize(anyString())).thenReturn(100);

        String order = "desc";

        GetTasksResponse result = service.getTasks(StatusType.DONE.toString(), order, 1, 10, builder);
        verify(mockRepository, times(1)).getTaskPage(anyString(),
                anyString(), anyInt(), anyInt());
        assertNull(result.getMeta().getPrev());
    }

    @Test
    public void testGetTasksSizeZero() {
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        when(mockRepository.getSize(anyString())).thenReturn(null);

        String order = "desc";

        GetTasksResponse result = service.getTasks(StatusType.DONE.toString(), order, 3, 20, builder);
        verify(mockRepository, never()).getTaskPage(anyString(),
                anyString(), anyInt(), anyInt());
        assertNull(result);
    }

    @Test
    public void testGetTasksInvalidData() {
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        String order = "desc";
        GetTasksResponse result = service.getTasks(StatusType.DONE.toString(), order, 3, 0, builder);
        verify(mockRepository, never()).getTaskPage(anyString(),
                anyString(), anyInt(), anyInt());
        assertNull(result);
    }

    @Test
    public void testGetTasksNoPageMax() {
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        when(mockRepository.getSize(anyString())).thenReturn(100);

        String order = "desc";

        GetTasksResponse result = service.getTasks(StatusType.DONE.toString(), order, 10, 20, builder);
        verify(mockRepository, never()).getTaskPage(anyString(),
                anyString(), anyInt(), anyInt());
        assertNull(result);
    }

    @Test
    public void testGetTasksNoPageEqual() {
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);

        when(builder.queryParam(anyString(), any())).thenReturn(builder);
        when(builder.replaceQueryParam(anyString(), any())).thenReturn(builder);
        when(builder.toUriString()).thenReturn("next").thenReturn("prev").thenReturn("first").thenReturn("last");

        when(mockRepository.getSize(anyString())).thenReturn(100);

        String order = "desc";

        GetTasksResponse result = service.getTasks(StatusType.DONE.toString(), order, 10, 10, builder);
        verify(mockRepository, times(1)).getTaskPage(anyString(),
                anyString(), anyInt(), anyInt());
        assertNull(result.getMeta().getNext());
    }
}
