package ru.practicum.ewm.event.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.practicum.ewm.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;

public class EventRequestMapper {

    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<ParticipationRequest> requests) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (ParticipationRequest req : requests) {
            if (req.getStatus().equals(Status.CONFIRMED)) {
                confirmedRequests.add(ParticipationRequestMapper
                        .toParticipationRequestDto(req));
            } else {
                rejectedRequests.add(ParticipationRequestMapper
                        .toParticipationRequestDto(req));
            }
        }

        return new EventRequestStatusUpdateResult(
              confirmedRequests,
              rejectedRequests
        );
    }

}
