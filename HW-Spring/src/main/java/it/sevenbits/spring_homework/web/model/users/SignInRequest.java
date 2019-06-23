package it.sevenbits.spring_homework.web.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User login request.
 */
public class SignInRequest {
    private final String login;
    private final String password;

    /**
     * Constructs request.
     * @param login username
     * @param password user's password
     */
    @JsonCreator
    public SignInRequest(final @JsonProperty("username") String login,
                         final @JsonProperty("password") String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
