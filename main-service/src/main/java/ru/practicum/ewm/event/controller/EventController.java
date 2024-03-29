package ru.practicum.ewm.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    // ------------------  PRIVATE  ------------------

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event saveEventPrivate(@Valid @RequestBody EventDto eventDto,
                                  @PathVariable long userId) {
        return eventService.saveEventPrivate(eventDto, userId);
    }

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEventsByUserPrivate(@PathVariable long userId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllEventsByUserPrivate(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public Event getEventByEventIdAndUserIdPrivate(@PathVariable long userId,
                                                                    @PathVariable long eventId) {
        return eventService.getEventByEventIdAndUserIdPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public Event patchEventByEventIdAndUserIdPrivate(@PathVariable long userId,
                                                                      @PathVariable long eventId,
                                                                      @Valid @RequestBody UpdateEventUserDto updatedEventDto) {
        return eventService.patchEventByEventIdAndUserIdPrivate(updatedEventDto, userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserEventRequestByUserIdPrivate(@PathVariable long userId,
                                                                            @PathVariable long eventId) {
        return eventService.getUserEventRequestByUserIdAndEventIdPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult patchUserEventRequestStatusByUserIdAndEventIdPrivate(
                                                                    @PathVariable long userId,
                                                                    @PathVariable long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return eventService.patchUserEventRequestStatusByUserIdAndEventIdPrivate(updateRequest, userId, eventId);
    }

    // ------------------  PUBLIC  ------------------

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getAllEventsPublic(HttpServletRequest request,
                                                     @RequestParam(required = false) String text,
                                                     @RequestParam(defaultValue = "0") long[] categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(required = false) Boolean onlyAvailable,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllEventsPublic(
                request, text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable,
                sort, from, size);
    }

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public Event getEventByIdPublic(@PathVariable long eventId, HttpServletRequest request) {
        return eventService.getEventByIdPublic(eventId, request);
    }

    // ------------------  ADMIN  ------------------

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getAllEventsAdmin(HttpServletRequest request,
                                                    @RequestParam(required = false) long[] users,
                                                    @RequestParam(required = false) String[] states,
                                                    @RequestParam(required = false) long[] categories,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllEventsAdmin(request, users, states,
                        categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public Event patchEventByIdAdmin(@PathVariable long eventId,
                                                      @Valid @RequestBody UpdateEventAdminDto updatedEventDto) {
        return eventService.patchEventDataAdmin(updatedEventDto, eventId);
    }

}
