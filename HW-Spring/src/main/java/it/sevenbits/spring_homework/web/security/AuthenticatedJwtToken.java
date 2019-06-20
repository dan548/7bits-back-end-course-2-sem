package it.sevenbits.spring_homework.web.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Authorization which holds subject and roles/authorities from the JWT token.
 */
class AuthenticatedJwtToken extends AbstractAuthenticationToken {

    private final String subject;

    AuthenticatedJwtToken(final String subject, final Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.subject = subject;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return subject;
    }

}
