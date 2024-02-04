package ru.practicum.ewm.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;

@Entity
@Table(
        name = "comment",
        schema = "public"
)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comment {

    @Id
    @SequenceGenerator(
            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "event_id",
            nullable = false
    )
    private Event event;

    @ManyToOne
    @JoinColumn(
            name = "commenter_id",
            nullable = false
    )
    private User user;

    @Column(
            name = "text",
            nullable = false
    )
    private String text;

    @Column(
            name = "posted_on",
            nullable = false
    )
    private String postedOn;

    public Comment(String text,
                   Event event,
                   User user,
                   String postedOn) {
        this.text = text;
        this.event = event;
        this.user = user;
        this.postedOn = postedOn;
    }

}
