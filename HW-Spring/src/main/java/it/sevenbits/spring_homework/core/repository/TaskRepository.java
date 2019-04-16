package it.sevenbits.spring_homework.core.repository;

import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.repository.database.DatabaseException;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;

import java.util.List;
import java.util.function.Predicate;

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
    Task findTaskById(String id) throws DatabaseException;

    /**
     * Creates a task with the title specified.
     *
     * @param request - task creating request object
     * @return the task created
     */
    Task create(AddTaskRequest request);

    /**
     * Removes a task with the id specified.
     *
     * @param id - id of the task to delete.
     */
    void removeTaskById(String id) throws DatabaseException;

    /**
     * Gets all tasks list.
     *
     * @return a list of all the tasks present.
     */
    List<Task> getAllTasks();

    /**
     * Gets tasks satisfying the filter specified.
     *
     * @param predicate - filter predicate
     * @return a filtered list of tasks.
     */
    List<Task> getTasksFiltered(Predicate<Task> predicate);

}
