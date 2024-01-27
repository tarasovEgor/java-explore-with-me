package ru.practicum.ewm.category.model;

import lombok.*;

import javax.persistence.*;

//@Data
@Entity
@Table(
        name = "category",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "category_name", columnNames = "name")
        }
)
@Getter
@Setter
@ToString
//@NoArgsConstructor
@RequiredArgsConstructor
public class Category {

    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
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

    public Category(String name) {
        this.name = name;
    }

}
