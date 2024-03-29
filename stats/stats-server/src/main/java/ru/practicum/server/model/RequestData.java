package ru.practicum.server.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "request_data",
        schema = "public"
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RequestData {

    @Id
    @SequenceGenerator(
            name = "request_data_sequence",
            sequenceName = "request_data_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "request_data_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "app",
            nullable = false
    )
    private String app;

    @Column(
            name = "uri",
            nullable = false
    )
    private String uri;

    @Column(
            name = "ip",
            nullable = false
    )
    private String ip;

    @Column(
            name = "requested_on",
            nullable = false
    )
    private String timestamp;

    public RequestData(String app,
                       String uri,
                       String ip,
                       String timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}