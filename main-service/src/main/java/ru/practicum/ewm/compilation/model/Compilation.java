package ru.practicum.ewm.compilation.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.ewm.event.model.Event;

@Data
@Entity
@Table(
        name  = "compilation",
        schema = "public"
)
@NoArgsConstructor
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

//    @ManyToMany
//    @JoinTable(
//            name = "compilation_event",
//            joinColumns = @JoinColumn(name = "compilation_id"),
//            inverseJoinColumns = @JoinColumn(name = "event_id")
//    )


    @OneToMany(mappedBy = "compilation")
    private List<Event> events;

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
