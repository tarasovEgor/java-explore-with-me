package ru.practicum.ewm.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.request.model.Status;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {

    private Long[] requestIds;
    private Status status;

    public EventRequestStatusUpdateRequest(Long[] requestsIds,
                                           Status status) {
        this.requestIds = requestsIds;
        this.status = status;

    }
}
