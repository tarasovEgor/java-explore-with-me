package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("ru.practicum.*")
@ComponentScan(basePackages = { "ru.practicum.*" })
@EntityScan("ru.practicum.*")
public class EwmService {
    public static void main(String[] args) {
        SpringApplication.run(EwmService.class, args);
    }
}