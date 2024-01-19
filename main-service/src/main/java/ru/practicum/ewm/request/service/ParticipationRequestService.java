package ru.practicum.ewm.request.service;

import org.springframework.http.ResponseEntity;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

public interface ParticipationRequestService {

    ResponseEntity<Object> saveParticipationRequestPrivate(ParticipationRequestDto participationRequestDto, long userId);

    ResponseEntity<Object> getParticipationRequestByUserIdPrivate(long userId);

    ResponseEntity<Object> cancelParticipationRequestByUserIdPrivate(long userId, long requestId);

}
