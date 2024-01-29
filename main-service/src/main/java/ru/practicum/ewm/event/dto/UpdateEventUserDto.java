package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserDto {

    @Size(min = 20, max = 2000, message = "Annotation can't be shorter than 20 or greater than 2000 characters.")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Description can't be shorter than 20 or greater than 7000 characters.")
    private String description;

    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;

    @Size(min = 3, max = 120, message = "Title can't be shorter than 3 or greater than 120 characters.")
    private String title;

}
