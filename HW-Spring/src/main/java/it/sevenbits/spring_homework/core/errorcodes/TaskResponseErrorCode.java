package it.sevenbits.spring_homework.core.errorcodes;

import it.sevenbits.spring_homework.core.model.Task;
import org.springframework.http.ResponseEntity;

public enum TaskResponseErrorCode {
    BAD_STATUS("Invalid status.", ResponseEntity.badRequest().build()),
    BAD_REQUEST("Invalid request.", ResponseEntity.badRequest().build()),
    NOT_FOUND("Element with such id is not found.", ResponseEntity.notFound().build()),
    OK("okay", null);

    private String errorCode;
    private ResponseEntity<Task> entity;

    TaskResponseErrorCode(final String errorCode, final ResponseEntity<Task> entity) {
        this.errorCode = errorCode;
        this.entity = entity;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ResponseEntity<Task> getEntity() {
        return entity;
    }
}
