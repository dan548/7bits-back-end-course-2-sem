package it.sevenbits.spring_homework.config;

import it.sevenbits.spring_homework.core.dategetter.DateGetter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {
    @Bean
    @Qualifier("dateGetter")
    @ConfigurationProperties(prefix = "utils")
    public DateGetter dateGetter() {
        return new DateGetter("");
    }
}
