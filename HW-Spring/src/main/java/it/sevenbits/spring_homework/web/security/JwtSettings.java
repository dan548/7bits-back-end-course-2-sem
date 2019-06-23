package it.sevenbits.spring_homework.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Settings to the JWT token.
 */
@Component
public class JwtSettings {

    //TODO: add work with refresh token
    private String tokenIssuer;
    private String tokenSigningKey;
    private int aTokenDuration;

    /**
     * Constructs JWT settings
     * @param tokenIssuer issuer
     * @param tokenSigningKey signing key
     * @param aTokenDuration duration
     */
    public JwtSettings(@Value("${jwt.issuer}") final String tokenIssuer,
                       @Value("${jwt.signingKey}") final String tokenSigningKey,
                       @Value("${jwt.aTokenDuration}") final int aTokenDuration) {
        this.tokenIssuer = tokenIssuer;
        this.tokenSigningKey = tokenSigningKey;
        this.aTokenDuration = aTokenDuration;
    }

    /**
     * Gets issuer
     * @return token issuer
     */
    public String getTokenIssuer() {
        return tokenIssuer;
    }

    /**
     * Gets signing key
     * @return token signing key
     */
    public byte[] getTokenSigningKey() {
        return tokenSigningKey.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Get expiration date
     * @return expiration date
     */
    public Duration getTokenExpiredIn() {
        return Duration.ofMinutes(aTokenDuration);
    }

}
