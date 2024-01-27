package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.EventWithLDT;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    public static Event toEvent(EventDto eventDto) {
        return new Event(
                eventDto.getAnnotation(),
                eventDto.getDescription(),
                eventDto.getEventDate(),
                eventDto.getLocation(),
                eventDto.getPaid(),
                eventDto.getParticipantLimit(),
                eventDto.getRequestModeration(),
                eventDto.getTitle()
        );
    }

    public static Event toEvent(EventWithLDT eventWithLDT) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String eventDate = eventWithLDT.getEventDate().format(formatter);

        return new Event(
                eventWithLDT.getId(),
                eventWithLDT.getAnnotation(),
                eventWithLDT.getCategory(),
                eventWithLDT.getConfirmedRequests(),
                eventWithLDT.getCreatedOn(),
                eventWithLDT.getDescription(),
                eventDate,
                eventWithLDT.getInitiator(),
                eventWithLDT.getLocation(),
                eventWithLDT.getPaid(),
                eventWithLDT.getParticipantLimit(),
                eventWithLDT.getPublishedOn(),
                eventWithLDT.getRequestModeration(),
                eventWithLDT.getState(),
                eventWithLDT.getTitle(),
                eventWithLDT.getViews()
        );
    }

    public static List<Event> toEventList(List<EventWithLDT> eventsWithLDT) {
        List<Event> events = new ArrayList<>();
        for (EventWithLDT e : eventsWithLDT) {
            events.add(toEvent(e));
        }
        return events;
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static List<EventShortDto> toEventShortDto(List<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event e : events) {
            dtos.add(toEventShortDto(e));
        }
        return dtos;
    }

    public static EventWithLDT toEventWithLDT(Event event) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new EventWithLDT(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                LocalDateTime.parse(event.getEventDate(), formatter),
                event.getInitiator(),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static List<EventWithLDT> toEventWithLDT(List<Event> events) {
        List<EventWithLDT> dtos = new ArrayList<>();

        for (Event e : events) {
            dtos.add(toEventWithLDT(e));
        }

        return dtos;
    }

}
