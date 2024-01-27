package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    // ---------------------   PRIVATE   --------------------- //
    Event saveEventPrivate(EventDto eventDto, long userId);

    List<EventShortDto> getAllEventsByUserPrivate(long userId, int from, int size);

    Event getEventByEventIdAndUserIdPrivate(long userId, long eventId);

    Event patchEventByEventIdAndUserIdPrivate(UpdateEventUserDto updateEventUserDto, long userId, long eventId);

    List<ParticipationRequestDto> getUserEventRequestByUserIdAndEventIdPrivate(long userId, long eventId);

    EventRequestStatusUpdateResult patchUserEventRequestStatusByUserIdAndEventIdPrivate(EventRequestStatusUpdateRequest updateRequest, long userId, long eventId);


    // ---------------------   PUBLIC   --------------------- //
    List<Event> getAllEventsPublic(HttpServletRequest request, String text, long[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    Event getEventByIdPublic(long eventId, HttpServletRequest request);


    // ---------------------   ADMIN   --------------------- //
    List<Event> getAllEventsAdmin(HttpServletRequest request, long[] users, String[] states, long[] categories, String rangeStart, String rangeEnd, int from, int size);

    Event patchEventDataAdmin(UpdateEventAdminDto updateEventAdminDto, long eventId);

}
