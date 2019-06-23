package it.sevenbits.spring_homework.web.model.users;

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
}
