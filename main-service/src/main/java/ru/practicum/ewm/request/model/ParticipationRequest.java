package ru.practicum.ewm.request.model;

import lombok.*;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(
        name = "request",
        schema = "public"
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@DynamicUpdate
public class ParticipationRequest {

    @Id
    @SequenceGenerator(
            name = "request_sequence",
            sequenceName = "request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "request_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "created_on",
            nullable = false
    )
    private String created;

    @ManyToOne
    @JoinColumn(
            name = "event_id",
            nullable = false
    )
    private Event event;

    @ManyToOne
    @JoinColumn(
            name = "requester_id",
            nullable = false
    )
    private User requester;

    @Column(
            name = "status"
    )
    @Enumerated(EnumType.STRING)
    private Status status;

    public ParticipationRequest(Event event,
                                User requester,
                                Status status,
                                String created) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }

}
