package it.sevenbits.spring_homework.config;

import it.sevenbits.spring_homework.core.service.dategetter.DateGetter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Services configuration.
 */
@Configuration
public class UtilsConfig {
    /**
     * Configuration of the date getter.
     * @return date getter
     */
    @Bean
    @Qualifier("dateGetter")
    @ConfigurationProperties(prefix = "utils")
    public DateGetter dateGetter() {
        return new DateGetter("");
    }
}
