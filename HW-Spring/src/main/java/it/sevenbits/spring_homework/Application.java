package it.sevenbits.spring_homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class.
 *
 * @since 1.0
 * @version 1.0
 * @author Daniil Polyakov
 */
@SpringBootApplication
public class Application {

    /**
     * Application's entry point.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
