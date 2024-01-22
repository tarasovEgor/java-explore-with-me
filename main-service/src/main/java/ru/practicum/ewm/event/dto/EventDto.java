package ru.practicum.ewm.event.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    @NotNull(message = "Annotation field can't be null.")
    @NotBlank(message = "Annotation field can't be blank.")
    @Size(min = 20, max = 2000, message = "Annotation can't be shorter than 20 or greater than 2000 characters.")
    private String annotation;

    @NotNull(message = "Category can't be null.")
    private Long category;

    @NotNull(message = "Description field can't be null.")
    @NotBlank(message = "Description field can't be blank.")
    @Size(min = 20, max = 7000, message = "Description can't be shorter than 20 or greater than 7000 characters.")
    private String description;

    @NotNull(message = "Event date can't be null.")
    @NotBlank(message = "Event date can't be blank.")
    private String eventDate;

    @NotNull(message = "Location can't be null.")
    private Location location;

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    @NotNull(message = "Title can't be null.")
    @NotBlank(message = "Title can't be blank.")
    @Size(min = 3, max = 120, message = "Title can't be shorter than 3 or greater than 120 characters.")
    private String title;
}
