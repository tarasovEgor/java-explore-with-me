package ru.practicum.ewm.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    public EventRequestStatusUpdateResult(List<ParticipationRequestDto> confirmedRequests,
                                           List<ParticipationRequestDto> rejectedRequests) {
        this.confirmedRequests = confirmedRequests;
        this.rejectedRequests = rejectedRequests;
    }

}
