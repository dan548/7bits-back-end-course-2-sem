package it.sevenbits.spring_homework.core.model.response;

import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.Task;

public class TaskResponse {

    private final Task task;
    private final TaskResponseErrorCode code;

    public TaskResponse(final Task task) {
        this.task = task;
        code = null;
    }

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
