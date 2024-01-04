package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableJpaRepositories("ru.practicum.ewm")
@ComponentScan(basePackages = { "ru.practicum.*" })
@EntityScan("ru.practicum.*")
public class EwmService {
    public static void main(String[] args) {
        SpringApplication.run(EwmService.class, args);
    }

}