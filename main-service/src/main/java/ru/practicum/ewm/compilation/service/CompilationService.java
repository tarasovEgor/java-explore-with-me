package ru.practicum.ewm.compilation.service;

import org.springframework.http.ResponseEntity;

import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

public interface CompilationService {

    ResponseEntity<Object> saveCompilationAdmin(NewCompilationDto newCompilationDto);

    ResponseEntity<Object> deleteCompilationAdmin(long compId);

    ResponseEntity<Object> patchCompilationAdmin(UpdateCompilationDto newCompilationDto, long compId);

    ResponseEntity<Object> getAllCompilationsPublic(Boolean pinned, int from, int size);

    ResponseEntity<Object> getCompilationByIdPublic(long compId);

}
