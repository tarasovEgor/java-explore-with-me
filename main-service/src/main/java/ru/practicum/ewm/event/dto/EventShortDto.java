package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private Long id;

    @NotNull
    private String annotation;

    @NotNull
    private Category category;

    private Integer confirmedRequests;

    @NotNull
    private String eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Boolean paid;

    @NotNull
    private String title;

    private Long views;

}
