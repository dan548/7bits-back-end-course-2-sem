package it.sevenbits.spring_homework.web.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to take JwtToken from the request header.
 */
public class HeaderJwtAuthFilter extends JwtAuthFilter {

    private static final Pattern BEARER_AUTH_PATTERN = Pattern.compile("^Bearer\\s+(.*)$");
    private static final int TOKEN_GROUP = 1;

    public HeaderJwtAuthFilter(RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    protected String takeToken(HttpServletRequest request) throws AuthenticationException {
        String authHeader = request.getHeader("Authorization");
        Matcher m = BEARER_AUTH_PATTERN.matcher(authHeader);
        if (m.matches()) {
            return m.group(TOKEN_GROUP);
        } else {
            throw new JwtAuthenticationException("Invalid Authorization header: " + authHeader);
        }
    }

}
