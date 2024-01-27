package ru.practicum.ewm.event.service;

import org.springframework.http.ResponseEntity;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    // ---------------------   PRIVATE   --------------------- //
    //ResponseEntity<Object> saveEventPrivate(EventDto eventDto, long userId);
    Event saveEventPrivate(EventDto eventDto, long userId);


    //ResponseEntity<Object> getAllEventsByUserPrivate(long userId, int from, int size);
    List<EventShortDto> getAllEventsByUserPrivate(long userId, int from, int size);

    //ResponseEntity<Object> getEventByEventIdAndUserIdPrivate(long userId, long eventId);
    Event getEventByEventIdAndUserIdPrivate(long userId, long eventId);

    //ResponseEntity<Object> patchEventByEventIdAndUserIdPrivate(UpdateEventUserDto updateEventUserDto, long userId, long eventId);
    Event patchEventByEventIdAndUserIdPrivate(UpdateEventUserDto updateEventUserDto, long userId, long eventId);

    //ResponseEntity<Object> getUserEventRequestByUserIdAndEventIdPrivate(long userId, long eventId);
    List<ParticipationRequestDto> getUserEventRequestByUserIdAndEventIdPrivate(long userId, long eventId);

    //ResponseEntity<Object> patchUserEventRequestStatusByUserIdAndEventIdPrivate(EventRequestStatusUpdateRequest updateRequest, long userId, long eventId);
    EventRequestStatusUpdateResult patchUserEventRequestStatusByUserIdAndEventIdPrivate(EventRequestStatusUpdateRequest updateRequest, long userId, long eventId);


    // ---------------------   PUBLIC   --------------------- //
    //ResponseEntity<Object> getAllEventsPublic(HttpServletRequest request, String text, long[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);
    List<Event> getAllEventsPublic(HttpServletRequest request, String text, long[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    //ResponseEntity<Object> getEventByIdPublic(long eventId, HttpServletRequest request);
    Event getEventByIdPublic(long eventId, HttpServletRequest request);


    // ---------------------   ADMIN   --------------------- //
    //ResponseEntity<Object> getAllEventsAdmin(HttpServletRequest request, long[] users, String[] states, long[] categories, String rangeStart, String rangeEnd, int from, int size);
    List<Event> getAllEventsAdmin(HttpServletRequest request, long[] users, String[] states, long[] categories, String rangeStart, String rangeEnd, int from, int size);

    //ResponseEntity<Object> patchEventDataAdmin(UpdateEventAdminDto updateEventAdminDto, long eventId);
    Event patchEventDataAdmin(UpdateEventAdminDto updateEventAdminDto, long eventId);

}
