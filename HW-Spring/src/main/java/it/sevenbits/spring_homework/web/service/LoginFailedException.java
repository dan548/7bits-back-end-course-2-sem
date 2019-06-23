package it.sevenbits.spring_homework.web.service;

import org.springframework.security.core.AuthenticationException;

/**
 * Authentication failed exception
 */
public class LoginFailedException extends AuthenticationException {

    /**
     * Constructs exception
     * @param message error message
     * @param cause exception caused by
     */
    public LoginFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs exception
     * @param message error message
     */
    public LoginFailedException(final String message) {
        super(message);
    }

}
