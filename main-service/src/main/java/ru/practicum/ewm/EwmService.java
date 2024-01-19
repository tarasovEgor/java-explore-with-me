package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

@SpringBootApplication
//@EnableJpaRepositories("ru.practicum.ewm")
@ComponentScan(basePackages = { "ru.practicum.*" })
@EntityScan("ru.practicum.*")
public class EwmService {
    public static void main(String[] args) {
        SpringApplication.run(EwmService.class, args);

//        final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        LocalDateTime ldt = LocalDateTime.now();
//        String formattedString = ldt.format(CUSTOM_FORMATTER);
//
//
//        String now = LocalDateTime.now().format(CUSTOM_FORMATTER);
//
//        System.out.println(now);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Event event = new Event();
//        event.setEventDate("2023-10-11 23:10:05");
//        LocalDateTime time = LocalDateTime.parse(event.getEventDate(), formatter);
//
//        System.out.println(time);


    }

}