package it.sevenbits.spring_homework.web.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Authorization which holds subject and roles/authorities from the JWT token.
 */
class AuthenticatedJwtToken extends AbstractAuthenticationToken {

    private final String id;

    AuthenticatedJwtToken(final String id, final Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.id = id;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

}
