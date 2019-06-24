package it.sevenbits.spring_homework.web.model.users;

import java.util.Objects;

/**
 * Request model for signing up.
 */
public class SignUpRequest {

    private final String username;
    private final String password;

    /**
     * Constructs request.
     * @param username login
     * @param password user's password
     */
    public SignUpRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignUpRequest that = (SignUpRequest) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
