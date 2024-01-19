package ru.practicum.ewm.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Location;

@Data
@NoArgsConstructor
public class UpdateEventAdminDto {

    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;

}
