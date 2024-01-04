package ru.practicum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViewStatsDto {

    private String app;
    private String uri;
    private Long hits = 0L;

    public ViewStatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

}
