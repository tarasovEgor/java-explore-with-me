package ru.practicum.ewm.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.ParticipationRequestService;

import java.util.List;

@RestController
public class ParticipationRequestController {

    private final ParticipationRequestService requestService;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto saveParticipationRequestPrivate(@PathVariable long userId,
                                                                   @RequestParam long eventId) {
        return requestService.saveParticipationRequestPrivate(userId, eventId);
    }

    @GetMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getParticipationRequestByUserIdPrivate(@PathVariable long userId) {
        return requestService.getParticipationRequestByUserIdPrivate(userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelParticipationRequestByUserIdPrivate(@PathVariable long userId,
                                                                            @PathVariable long requestId) {
        return requestService.cancelParticipationRequestByUserIdPrivate(userId, requestId);
    }

}
