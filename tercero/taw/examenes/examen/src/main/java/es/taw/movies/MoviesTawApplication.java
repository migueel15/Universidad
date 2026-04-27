package es.taw.movies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviesTawApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesTawApplication.class, args);
    }

}
