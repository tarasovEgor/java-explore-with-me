package ru.practicum.ewm.request.mapper;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.model.User;

public class ParticipationRequestMapper {

    public static ParticipationRequest toParticipantRequest(User requester, Event event, Status status) {

        return new ParticipationRequest(
                event,
                requester,
                status
        );

    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {

        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus()
        );

    }
}
