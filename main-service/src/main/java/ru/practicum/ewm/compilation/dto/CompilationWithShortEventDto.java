package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationWithShortEventDto {

    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;

}
