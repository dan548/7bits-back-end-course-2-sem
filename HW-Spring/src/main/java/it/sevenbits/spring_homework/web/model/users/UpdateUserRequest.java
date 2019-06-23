package it.sevenbits.spring_homework.web.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * User updating request.
 */
public class UpdateUserRequest {

    private final Boolean status;
    private final List<String> authorities;

    /**
     * Constructs request.
     * @param status new enable status
     * @param authorities new user's authorities
     */
    @JsonCreator
    public UpdateUserRequest(final @JsonProperty("status") Boolean status,
                             final @JsonProperty("authorities") List<String> authorities) {
        this.status = status;
        this.authorities = authorities;
    }

    public Boolean getStatus() {
        return status;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
