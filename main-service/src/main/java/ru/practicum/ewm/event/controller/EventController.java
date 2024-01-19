package ru.practicum.ewm.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.UpdateEventAdminDto;
import ru.practicum.ewm.event.dto.UpdateEventUserDto;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    // ------------------  PRIVATE  ------------------

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<Object> saveEventPrivate(@RequestBody EventDto eventDto,
                                                   @PathVariable long userId) {
        return eventService.saveEventPrivate(eventDto, userId);
    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<Object> getAllEventsByUserPrivate(@PathVariable long userId,
                                                            @RequestParam(required = false, defaultValue = "0") int from,
                                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return eventService.getAllEventsByUserPrivate(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<Object> getEventByEventIdAndUserIdPrivate(@PathVariable long userId,
                                                                    @PathVariable long eventId) {
        return eventService.getEventByEventIdAndUserIdPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<Object> updateEventByEventIdAndUserIdPrivate(@PathVariable long userId,
                                                                       @PathVariable long eventId,
                                                                       @RequestBody UpdateEventUserDto updatedEventDto) {
        return eventService.patchEventByEventIdAndUserIdPrivate(updatedEventDto, userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> getUserEventRequestByUserIdPrivate(@PathVariable long userId,
                                                                    @PathVariable long eventId) {
        return eventService.getUserEventRequestByUserIdAndEventIdPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> patchUserEventRequestStatusByUserIdAndEventIdPrivate(@PathVariable long userId,
                                                                                       @PathVariable long eventId,
                                                                                       @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return eventService.patchUserEventRequestStatusByUserIdAndEventIdPrivate(updateRequest, userId, eventId);
    }

    // ------------------  PUBLIC  ------------------
    @GetMapping("/events")
    public ResponseEntity<Object> getAllEventsPublic(HttpServletRequest request,
                                                     @RequestParam(required = false) String text,
                                                     @RequestParam(required = false, defaultValue = "0") long[] categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(required = false) Boolean onlyAvailable,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(required = false, defaultValue = "0") int from,
                                                     @RequestParam(required = false, defaultValue = "10") int size) {
        return eventService
                .getAllEventsPublic(request, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getEventByIdPublic(@PathVariable long eventId, HttpServletRequest request) {
        return eventService.getEventByIdPublic(eventId, request);
    }


    // ------------------  ADMIN  ------------------

    @GetMapping("/admin/events")
    public ResponseEntity<Object> getAllEventsAdmin(HttpServletRequest request,
                                                    @RequestParam(required = false) long[] users,
                                                    @RequestParam(required = false) String[] states,
                                                    @RequestParam(required = false) long[] categories,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(required = false, defaultValue = "0") int from,
                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        return eventService.getAllEventsAdmin(request, users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /*HttpServletRequest request, long[] users,
                                                    String[] statesStr, long[] categories,
                                                    String rangeStart, String rangeEnd,
                                                    int from, int size*/
    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<Object> updateEventByIdAdmin(@PathVariable long eventId,
                                                       @RequestBody UpdateEventAdminDto updatedEventDto) {
        return eventService.updateEventDataPublic(updatedEventDto, eventId);
    }



    /*String text, long[] categories, Boolean paid,
                                                     String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                     String sort, int from, int size*/
}
