package it.sevenbits.spring_homework.core.repository;

import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;

import java.util.List;

/**
 * Task repository interface.
 *
 * @since 1.0
 * @version 1.0
 * @author Daniil Polyakov
 */
public interface TaskRepository {

    /**
     * Finds a task with the id specified.
     *
     * @param id - id of the task to find
     * @return task with the specified id
     */
    Task findTaskById(String id);

    /**
     * Creates a task with the title specified.
     *
     * @param request - task creating request object
     * @return the task created
     */
    Task create(AddTaskRequest request);

    /**
     * Updates a task with the id specified.
     *
     * @param request - task updating request object
     * @param id id of the task to update
     * @return number of tasks changed
     */
    int editTaskById(UpdateTaskRequest request, String id);

    /**
     * Removes a task with the id specified.
     *
     * @param id - id of the task to delete.
     * @return response saying if the task was deleted
     */
    Task removeTaskById(String id);

    /**
     * Gets all tasks list.
     *
     * @return a list of all the tasks present.
     */
    List<Task> getAllTasks();

    /**
     * Gets tasks with the specified status.
     *
     * @param status status of the tasks to get
     * @return a filtered list of tasks.
     */
    List<Task> getTasksWithStatus(String status);

    /**
     * Gets a page of tasks.
     *
     * @param status status of the tasks to get
     * @param order order of tasks
     * @param page current page
     * @param size page size
     * @return page of tasks
     */
    List<Task> getTaskPage(String status, String order, int page, int size);

    /**
     * Gets task count.
     * @param status status of tasks
     * @return task count
     */
    Integer getSize(String status);

}
