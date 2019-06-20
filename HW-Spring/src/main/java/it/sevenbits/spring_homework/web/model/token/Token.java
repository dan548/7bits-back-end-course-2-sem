package it.sevenbits.spring_homework.web.model.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

    private final String token;

    @JsonCreator
    public Token(final @JsonProperty("token") String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
