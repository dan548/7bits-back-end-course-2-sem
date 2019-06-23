package it.sevenbits.spring_homework.web.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Generic exception related to Jwt.
 */
class JwtAuthenticationException extends AuthenticationException {

    /**
     * Constructs exception
     * @param message error message
     */
    JwtAuthenticationException(final String message) {
        super(message);
    }

    /**
     * Constructs exception
     * @param message error message
     * @param cause error caused by
     */
    JwtAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
