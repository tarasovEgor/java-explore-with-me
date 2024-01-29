package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto saveParticipationRequestPrivate(long userId, long eventId);

    List<ParticipationRequestDto> getParticipationRequestByUserIdPrivate(long userId);

    ParticipationRequestDto cancelParticipationRequestByUserIdPrivate(long userId, long requestId);

}
