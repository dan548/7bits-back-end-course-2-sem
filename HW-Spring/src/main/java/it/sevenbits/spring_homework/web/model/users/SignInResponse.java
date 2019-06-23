package it.sevenbits.spring_homework.web.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for login.
 */
public class SignInResponse {

    private final String token;

    /**
     * Constructs response.
     * @param token String JWT token
     */
    @JsonCreator
    public SignInResponse(final @JsonProperty("token") String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
