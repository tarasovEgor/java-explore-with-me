package ru.practicum.ewm.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.*;

public class EventValidation {

    public static boolean isEventDateValidForUpdate(String eventDate, String newEventDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime eventDateLDT = parse(eventDate, formatter);
        LocalDateTime newEventDateLDT = parse(newEventDate, formatter);

        return eventDateLDT.isBefore(LocalDateTime.now().minusHours(1))
                || eventDateLDT.equals(newEventDateLDT);

    }

    public static boolean isEventDateValidForSave(String eventDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime eventDateLDT = parse(eventDate, formatter);

        return eventDateLDT.isAfter(LocalDateTime.now().minusHours(2));

    }
}
