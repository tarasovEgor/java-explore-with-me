package ru.practicum.ewm.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.request.service.ParticipationRequestService;

@RestController
public class ParticipationRequestController {

    private final ParticipationRequestService requestService;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<?> saveParticipationRequestPrivate(@PathVariable long userId,
                                                                  @RequestParam long eventId) {
        //return requestService.saveParticipationRequestPrivate(userId, eventId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestService.saveParticipationRequestPrivate(userId, eventId));
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<?> getParticipationRequestByUserIdPrivate(@PathVariable long userId) {
        //return requestService.getParticipationRequestByUserIdPrivate(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.getParticipationRequestByUserIdPrivate(userId));
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<?> cancelParticipationRequestByUserIdPrivate(@PathVariable long userId,
                                                                            @PathVariable long requestId) {
        //return requestService.cancelParticipationRequestByUserIdPrivate(userId, requestId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.cancelParticipationRequestByUserIdPrivate(userId, requestId));
    }

}
