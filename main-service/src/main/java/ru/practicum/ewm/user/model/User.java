package ru.practicum.ewm.user.model;

import lombok.*;

import javax.persistence.*;

//@Data
@Entity
@Table(
        name = "users",
        schema = "public"
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
//@NoArgsConstructor
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            schema = "public",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "email",
            nullable = false
    )
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
