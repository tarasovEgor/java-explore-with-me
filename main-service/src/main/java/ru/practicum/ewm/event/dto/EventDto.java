package ru.practicum.ewm.event.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.event.model.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
