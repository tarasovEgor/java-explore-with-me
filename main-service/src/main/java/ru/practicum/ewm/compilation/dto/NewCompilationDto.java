package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private List<Long> events;
    private Boolean pinned;
    private String title;

}
