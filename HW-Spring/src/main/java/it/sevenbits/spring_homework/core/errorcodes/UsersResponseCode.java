package it.sevenbits.spring_homework.core.errorcodes;

/**
 * Enum of user service response codes
 */
public enum UsersResponseCode {

    /**
     * Request executing was successful;
     * Request was invalid;
     * Bad request;
     * Request username already exists
     */
    SUCCESS, INVALID, NOT_FOUND, EXISTS

}
