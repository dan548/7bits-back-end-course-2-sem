package it.sevenbits.spring_homework.web.security;

import it.sevenbits.spring_homework.web.model.users.User;
import org.springframework.security.core.Authentication;

import java.time.Duration;

/**
 * Base JWT service interface
 */
public interface JwtTokenService {
    /**
     * Getter for token duration
     * @return token duration
     */
    Duration getTokenExpiredIn();
    /**
     * Parses the token
     * @param token the token string to parse
     * @return authenticated data
     */
    Authentication parseToken(String token);

    /**
     * Creates new SignInResponse for user.
     * @param user contains User to be represented as token
     * @return signed token
     */
    String createToken(User user);
}
