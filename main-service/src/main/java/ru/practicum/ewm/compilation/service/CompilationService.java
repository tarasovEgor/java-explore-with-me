package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationWithShortEventDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationWithShortEventDto saveCompilationAdmin(NewCompilationDto newCompilationDto);

    void deleteCompilationAdmin(long compId);

    CompilationWithShortEventDto patchCompilationAdmin(UpdateCompilationDto newCompilationDto, long compId);

    List<CompilationWithShortEventDto> getAllCompilationsPublic(Boolean pinned, int from, int size);

    CompilationWithShortEventDto getCompilationByIdPublic(long compId);

}
