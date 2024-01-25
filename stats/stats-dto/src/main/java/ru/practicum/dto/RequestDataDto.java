package ru.practicum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestDataDto {

    private String app;
    private String uri;
    private String ip;
    private String timestamp;

    public RequestDataDto(
                          String app,
                          String uri,
                          String ip,
                          String timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

}
