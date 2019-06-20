package it.sevenbits.spring_homework.web.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class User {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("username")
    private final String username;

    @JsonProperty("authorities")
    private final List<String> authorities;

    @JsonIgnore
    private final String password;

    public User(String id, String username, String password, List<String> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.password = password;
    }

    @JsonCreator
    public User(String id, String username, List<String> authorities) {
        this.id = id;
        this.username = username;
        this.password = null;
        this.authorities = authorities;
    }

    public User(final Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            id = null;
        } else {
            id = principal.toString();
            username = null;
        }
        password = null;
        authorities = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
    }

    public String getUsername() {
        return username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }
}
