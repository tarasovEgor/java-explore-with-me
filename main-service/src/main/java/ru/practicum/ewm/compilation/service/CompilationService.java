package ru.practicum.ewm.compilation.service;

import org.springframework.http.ResponseEntity;

import ru.practicum.ewm.compilation.dto.NewCompilationDto;

public interface CompilationService {

    ResponseEntity<Object> saveCompilationAdmin(NewCompilationDto newCompilationDto);

    ResponseEntity<Object> deleteCompilationAdmin(long compId);

    ResponseEntity<Object> patchCompilationAdmin(NewCompilationDto newCompilationDto, long compId);

    ResponseEntity<Object> getAllCompilationsPublic(Boolean pinned, int from, int size);

    ResponseEntity<Object> getCompilationByIdPublic(long compId);

}
