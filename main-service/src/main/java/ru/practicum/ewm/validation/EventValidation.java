package ru.practicum.ewm.validation;

import ru.practicum.ewm.event.dto.UpdateEventAdminDto;
import ru.practicum.ewm.event.dto.UpdateEventUserDto;
import ru.practicum.ewm.event.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.*;

public class EventValidation {

    /*
        - Дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
        - Событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
        - Событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
        */

    public static Boolean isEventDateValidForUpdate(UpdateEventAdminDto updateEventAdminDto, Event event) {

//        if (updateEventAdminDto.getEventDate() == null) {
//            return true;
//        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime oldEventDate = parse(event.getEventDate(), formatter);
        LocalDateTime newEventTime;

//        if (updateEventAdminDto.getEventDate() != null) {
//            newEventTime = parse(updateEventAdminDto.getEventDate(), formatter);
//            return oldEventDate.isBefore(LocalDateTime.now().minusMinutes(60))
//                    && newEventTime.isBefore(LocalDateTime.now().minusMinutes(60));
//        } else {
//            return false;
//        }


        newEventTime = parse(updateEventAdminDto.getEventDate(), formatter);
//        return oldEventDate.isBefore(LocalDateTime.now().minusMinutes(60))
//                && newEventTime.isBefore(LocalDateTime.now().minusMinutes(60));

        return true;


//        if (oldEventDate.isBefore(LocalDateTime.now().minusMinutes(60))
//                && ) {
//            return true;
//        }
//        return false;

    }

    public static boolean isEventDateValidForUpdate(UpdateEventUserDto updateEventUserDto, Event event) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime oldEventDate = parse(event.getEventDate(), formatter);
        LocalDateTime newEventTime;

        newEventTime = parse(updateEventUserDto.getEventDate(), formatter);
        return oldEventDate.isBefore(LocalDateTime.now().minusMinutes(60))
                && newEventTime.isBefore(LocalDateTime.now().minusMinutes(60));
    }

    public static boolean isEventDateValidForUpdate(String eventDate, String newEventDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime eventDateLDT = parse(eventDate, formatter);
        LocalDateTime newEventDateLDT = parse(newEventDate, formatter);

        return eventDateLDT.isBefore(LocalDateTime.now().minusHours(1))
                || eventDateLDT.equals(newEventDateLDT);

    }
}
