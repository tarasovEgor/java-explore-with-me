package ru.practicum.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class RequestDataDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
    private Long hits = 0L;

    public RequestDataDto(Long id,
                          String app,
                          String uri,
                          String ip,
                          String timestamp,
                          Long hits) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
        this.hits = hits;
    }

//    public RequestDataDto(String app,
//                          String uri,
//                          Long hits) {
//        this.app = app;
//        this.uri = uri;
//        this.hits = hits;
//    }

}
