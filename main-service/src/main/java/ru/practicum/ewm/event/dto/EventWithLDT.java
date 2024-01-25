package ru.practicum.ewm.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventWithLDT {

    private Long id;
    private String annotation;
    private Category category;
    private Integer confirmedRequests = 0;
    private String createdOn;
    private String description;
    private LocalDateTime eventDate;
    private User initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views = 0L;

    public EventWithLDT(Long id,
                        String annotation,
                        Category category,
                        Integer confirmedRequests,
                        String createdOn,
                        String description,
                        LocalDateTime eventDate,
                        User initiator,
                        Location location,
                        Boolean paid,
                        Integer participantLimit,
                        LocalDateTime publishedOn,
                        Boolean requestModeration,
                        State state,
                        String title,
                        Long views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
    }

}
