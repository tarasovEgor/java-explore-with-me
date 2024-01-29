package ru.practicum.ewm.event.model;

import lombok.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "events",
        schema = "public"
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Event {

    @Id
    @SequenceGenerator(
            name = "event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "annotation"
    )
    private String annotation;

    @ManyToOne
    @JoinColumn(
            name = "category_id"
    )
    private Category category;

    @Column(
            name = "confirmed_req_num"
    )
    private Integer confirmedRequests = 0;

    @Column(
            name = "created_on"
    )
    private String createdOn;

    @Column(
            name = "description"
    )
    private String description;

    @Column(
            name = "event_date"
    )
    private String eventDate;

    @ManyToOne
    @JoinColumn(
            name = "initiator_id"
    )
    private User initiator;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "location_id"
    )
    private Location location;

    @Column(
            name = "paid"
    )
    private Boolean paid;

    @Column(
            name = "participant_lim"
    )
    private Integer participantLimit;

    @Column(
            name = "published_on"
    )
    private LocalDateTime publishedOn;

    @Column(
            name = "req_mod_is_required"
    )
    private Boolean requestModeration;

    @Column(
            name = "state"
    )
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(
            name = "title"
    )
    private String title;

    @Column(
            name = "views"
    )
    private Long views = 0L;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "compilation_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id"))
    private List<Compilation> compilations = new ArrayList<>();

    public Event(String annotation,
                 String description,
                 String eventDate,
                 Location location,
                 Boolean paid,
                 Integer participantLimit,
                 Boolean requestModeration,
                 String title
    ) {
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }

    public Event(Long id,
                 String annotation,
                 Category category,
                 Integer confirmedRequests,
                 String createdOn,
                 String description,
                 String eventDate,
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
