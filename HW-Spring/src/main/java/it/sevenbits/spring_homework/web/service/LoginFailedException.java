package it.sevenbits.spring_homework.web.service;

import org.springframework.security.core.AuthenticationException;

/**
 * Authentication failed exception
 */
class LoginFailedException extends AuthenticationException {
    /**
     * Constructs exception
     * @param message error message
     */
    LoginFailedException(final String message) {
        super(message);
    }

}
