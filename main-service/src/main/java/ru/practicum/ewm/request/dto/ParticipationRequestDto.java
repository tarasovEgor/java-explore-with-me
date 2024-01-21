package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.request.model.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id;
    private Long requester;
    private Long event;
    private Status status;
    private LocalDateTime created;

    public ParticipationRequestDto(Long event) {
        this.event = event;
    }

    public ParticipationRequestDto(Long id,
                                   Long event,
                                   Long requester,
                                   Status status,
                                   LocalDateTime created) {
        this.id = id;
        this.event = event;
        this.requester = requester;
        this.status = status;
        this.created = created;
    }

}
