package it.sevenbits.spring_homework.core.model.response;

import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.Task;

/**
 * Model of the task response.
 */
public class TaskResponse {

    private final Task task;
    private final TaskResponseErrorCode code;

    /**
     * Constructs response with a task.
     * @param task the task to wrap
     */
    public TaskResponse(final Task task) {
        this.task = task;
        code = null;
    }

    /**
     * Constructs response with error code.
     * @param code the code to wrap
     */
    public TaskResponse(final TaskResponseErrorCode code) {
        this.task = null;
        this.code = code;
    }

    public Task getTask() {
        return task;
    }

    public TaskResponseErrorCode getCode() {
        return code;
    }
}
