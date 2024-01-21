package ru.practicum.ewm.request.mapper;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParticipationRequestMapper {

    public static ParticipationRequest toParticipantRequest(Event event, User requester, Status status, String created) {

        return new ParticipationRequest(
                event,
                requester,
                status,
                created
        );

    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime createdOn = LocalDateTime.parse(participationRequest.getCreated(), formatter);

        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus(),
                createdOn
        );

    }
}
