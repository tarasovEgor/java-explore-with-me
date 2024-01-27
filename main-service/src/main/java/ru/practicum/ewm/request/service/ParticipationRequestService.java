package ru.practicum.ewm.request.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

   // ResponseEntity<Object> saveParticipationRequestPrivate(long userId, long eventId);
    ParticipationRequestDto saveParticipationRequestPrivate(long userId, long eventId);

    //ResponseEntity<Object> getParticipationRequestByUserIdPrivate(long userId);
    List<ParticipationRequestDto> getParticipationRequestByUserIdPrivate(long userId);

   // ResponseEntity<Object> cancelParticipationRequestByUserIdPrivate(long userId, long requestId);

    ParticipationRequestDto cancelParticipationRequestByUserIdPrivate(long userId, long requestId);

}
