package ru.practicum.ewm.event.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "location",
        schema = "public"
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Location {

    @Id
    @SequenceGenerator(
            name = "location_sequence",
            sequenceName = "location_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "lat",
            nullable = false
    )
    private float lat;

    @Column(
            name = "lon",
            nullable = false
    )
    private float lon;

    public Location(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

}
