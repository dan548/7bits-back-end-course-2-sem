package it.sevenbits.spring_homework.web.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateUserRequest {

    private final Boolean status;
    private final List<String> authorities;

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
