package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationDto {
    private List<Long> events;
    private Boolean pinned;

    @Size(min = 1, max = 50, message = "Title can't be shorter than 1 or greater than 50 characters.")
    private String title;
}
