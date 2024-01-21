package ru.practicum.ewm.request.service;

import org.springframework.http.ResponseEntity;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

public interface ParticipationRequestService {

    ResponseEntity<Object> saveParticipationRequestPrivate(long userId, long eventId);

    ResponseEntity<Object> getParticipationRequestByUserIdPrivate(long userId);

    ResponseEntity<Object> cancelParticipationRequestByUserIdPrivate(long userId, long requestId);

}
