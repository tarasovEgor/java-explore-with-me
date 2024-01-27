package ru.practicum.ewm.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.UpdateEventAdminDto;
import ru.practicum.ewm.event.dto.UpdateEventUserDto;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    // ------------------  PRIVATE  ------------------

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<?> saveEventPrivate(@Valid @RequestBody EventDto eventDto,
                                                   @PathVariable long userId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.saveEventPrivate(eventDto, userId));
    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<?> getAllEventsByUserPrivate(@PathVariable long userId,
                                                            @RequestParam(defaultValue = "0") int from,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getAllEventsByUserPrivate(userId, from, size));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<?> getEventByEventIdAndUserIdPrivate(@PathVariable long userId,
                                                                    @PathVariable long eventId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEventByEventIdAndUserIdPrivate(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<?> patchEventByEventIdAndUserIdPrivate(@PathVariable long userId,
                                                                      @PathVariable long eventId,
                                                                      @Valid @RequestBody UpdateEventUserDto updatedEventDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchEventByEventIdAndUserIdPrivate(updatedEventDto, userId, eventId));
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<?> getUserEventRequestByUserIdPrivate(@PathVariable long userId,
                                                                     @PathVariable long eventId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getUserEventRequestByUserIdAndEventIdPrivate(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<?> patchUserEventRequestStatusByUserIdAndEventIdPrivate(
                                                                    @PathVariable long userId,
                                                                    @PathVariable long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchUserEventRequestStatusByUserIdAndEventIdPrivate(updateRequest, userId, eventId));
    }

    // ------------------  PUBLIC  ------------------

    @GetMapping("/events")
    public ResponseEntity<?> getAllEventsPublic(HttpServletRequest request,
                                                     @RequestParam(required = false) String text,
                                                     @RequestParam(defaultValue = "0") long[] categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(required = false) Boolean onlyAvailable,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getAllEventsPublic(
                        request, text, categories, paid,
                        rangeStart, rangeEnd, onlyAvailable,
                        sort, from, size)
                );
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<?> getEventByIdPublic(@PathVariable long eventId, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEventByIdPublic(eventId, request));
    }

    // ------------------  ADMIN  ------------------

    @GetMapping("/admin/events")
    public ResponseEntity<?> getAllEventsAdmin(HttpServletRequest request,
                                                    @RequestParam(required = false) long[] users,
                                                    @RequestParam(required = false) String[] states,
                                                    @RequestParam(required = false) long[] categories,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getAllEventsAdmin(request, users, states,
                        categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<?> patchEventByIdAdmin(@PathVariable long eventId,
                                                      @Valid @RequestBody UpdateEventAdminDto updatedEventDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchEventDataAdmin(updatedEventDto, eventId));
    }

}
