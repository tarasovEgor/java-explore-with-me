package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationWithShortEventDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {

    //ResponseEntity<Object> saveCompilationAdmin(NewCompilationDto newCompilationDto);

    CompilationWithShortEventDto saveCompilationAdmin(NewCompilationDto newCompilationDto);


//    ResponseEntity<Object> deleteCompilationAdmin(long compId);

    void deleteCompilationAdmin(long compId);

  //  ResponseEntity<Object> patchCompilationAdmin(UpdateCompilationDto newCompilationDto, long compId);

    CompilationWithShortEventDto patchCompilationAdmin(UpdateCompilationDto newCompilationDto, long compId);

//    ResponseEntity<Object> getAllCompilationsPublic(Boolean pinned, int from, int size);

    List<CompilationWithShortEventDto> getAllCompilationsPublic(Boolean pinned, int from, int size);

    //ResponseEntity<Object> getCompilationByIdPublic(long compId);

    CompilationWithShortEventDto getCompilationByIdPublic(long compId);

}
