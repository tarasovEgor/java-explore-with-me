package ru.practicum.ewm.compilation.model;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import ru.practicum.ewm.event.model.Event;

@Entity
@Table(
        name  = "compilation",
        schema = "public"
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@DynamicUpdate
public class Compilation {

    @Id
    @SequenceGenerator(
            name = "compilation_sequence",
            sequenceName = "compilation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "compilation_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "compilation_event",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events = new ArrayList<>();

    @Column(
            name = "pinned"
    )
    private Boolean pinned;

    @Column(
            name = "title"
    )
    private String title;

    public Compilation(List<Event> events,
                       Boolean pinned,
                       String title) {
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }
}
