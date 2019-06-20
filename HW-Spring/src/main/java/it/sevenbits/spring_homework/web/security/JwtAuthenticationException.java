package it.sevenbits.spring_homework.web.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Generic exception related to Jwt.
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
