package it.sevenbits.spring_homework.web.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User model.
 */
public class User {

    @JsonProperty("id")
    private String id;

    @JsonProperty("username")
    private final String username;

    @JsonProperty("authorities")
    private final List<String> authorities;

    @JsonIgnore
    private final String password;

    /**
     * Constructs new user.
     * @param id user id
     * @param username username
     * @param password user's password
     * @param authorities user's current authorities
     */
    public User(final String id, final String username, final String password, final List<String> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.password = password;
    }

    /**
     * Constructs new user for JSON generating.
     * @param id user id
     * @param username username
     * @param authorities user's current authorities
     */
    @JsonCreator
    public User(final String id, final String username, final List<String> authorities) {
        this.id = id;
        this.username = username;
        this.password = null;
        this.authorities = authorities;
    }

    /**
     * Gets a user by the current authentication.
     * @param authentication current authentication
     */
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

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(authorities, user.authorities) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, authorities, password);
    }
}
