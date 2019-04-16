package it.sevenbits.spring_homework.core.repository.database;

/**
 * Exception of database.
 */
public class DatabaseException extends Exception {

    /**
     * Constructs an exception
     * @param message error message
     */
    public DatabaseException(final String message) {
        super(message);
    }
}
