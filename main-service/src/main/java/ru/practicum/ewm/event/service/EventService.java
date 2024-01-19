package ru.practicum.ewm.event.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.UpdateEventAdminDto;
import ru.practicum.ewm.event.dto.UpdateEventUserDto;

import javax.servlet.http.HttpServletRequest;

public interface EventService {

    // ---------------------   PRIVATE   --------------------- //
    ResponseEntity<Object> saveEventPrivate(EventDto eventDto, long userId);

    ResponseEntity<Object> getAllEventsByUserPrivate(long userId, int from, int size);

    ResponseEntity<Object> getEventByEventIdAndUserIdPrivate(long userId, long eventId);

    ResponseEntity<Object> patchEventByEventIdAndUserIdPrivate(UpdateEventUserDto updateEventUserDto, long userId, long eventId);

    ResponseEntity<Object> getUserEventRequestByUserIdAndEventIdPrivate(long userId, long eventId);

    ResponseEntity<Object> patchUserEventRequestStatusByUserIdAndEventIdPrivate(EventRequestStatusUpdateRequest updateRequest, long userId, long eventId);



    // ---------------------   PUBLIC   --------------------- //

    ResponseEntity<Object> getAllEventsPublic(HttpServletRequest request, String text,
                                              long[] categories, Boolean paid,
                                              String rangeStart, String rangeEnd,
                                              Boolean onlyAvailable, String sort,
                                              int from, int size);

    ResponseEntity<Object> getEventByIdPublic(long eventId, HttpServletRequest request);

    // ---------------------   ADMIN   --------------------- //

    ResponseEntity<Object> getAllEventsAdmin(long[] users, String[] states, long[] categories,  // GET: /admin/events
                                             String rangeStart, String rangeEnd,
                                             int from, int size);

    ResponseEntity<Object> updateEventDataPublic(UpdateEventAdminDto updateEventAdminDto, long eventId);

}
