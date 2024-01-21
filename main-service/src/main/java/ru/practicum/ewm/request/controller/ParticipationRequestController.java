package ru.practicum.ewm.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> saveParticipationRequestPrivate(@PathVariable long userId,
                                                                  @RequestParam long eventId) {
                                                                   //@RequestBody ParticipationRequestDto requestDto) {
        return requestService.saveParticipationRequestPrivate(userId, eventId);
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<Object> getParticipationRequestByUserIdPrivate(@PathVariable long userId) {
        return requestService.getParticipationRequestByUserIdPrivate(userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelParticipationRequestByUserIdPrivate(@PathVariable long userId,
                                                                            @PathVariable long requestId) {
        return requestService.cancelParticipationRequestByUserIdPrivate(userId, requestId);
    }
}
