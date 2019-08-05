package it.sevenbits.spring_homework.config;

import it.sevenbits.spring_homework.web.security.HeaderJwtAuthFilter;
import it.sevenbits.spring_homework.web.security.JwtAuthFilter;
import it.sevenbits.spring_homework.web.security.JwtTokenService;
import it.sevenbits.spring_homework.web.security.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Security configuration
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenService jwtTokenService;

    /**
     * Constructs configuration by token service
     * @param jwtTokenService JWT processing service
     */
    public WebSecurityConfig(final JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Specifies password encoder
     * @return specific encoder implementation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable();
        http.logout().disable();
        http.sessionManagement().disable();
        http.requestCache().disable();
        http.anonymous();

        RequestMatcher signUpPageMatcher = new AntPathRequestMatcher("/signup");
        RequestMatcher notSignUpPageMatcher = new NegatedRequestMatcher(signUpPageMatcher);
        JwtAuthFilter authFilter = new HeaderJwtAuthFilter(notSignUpPageMatcher);
        http.addFilterBefore(authFilter, FilterSecurityInterceptor.class);

        http
                .cors()
                .and()
                .authorizeRequests().antMatchers("/users/**").hasAuthority("ADMIN")
                .and()
                .authorizeRequests().antMatchers("/whoami").hasAuthority("USER")
                .and()
                .authorizeRequests().antMatchers("/tasks/**").hasAuthority("USER")
                .and()
                .authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTokenService));
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/signin");
        web.ignoring().antMatchers("/signup");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Origin", "Accept", "Key",
                "DNT", "Keep-Alive", "User-Agent", "X-Requested-With",
                "If-Modified-Since", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Location"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
