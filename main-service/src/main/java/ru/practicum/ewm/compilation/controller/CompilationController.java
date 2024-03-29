package ru.practicum.ewm.compilation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ru.practicum.ewm.compilation.dto.CompilationWithShortEventDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;

import java.util.List;

@RestController
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping("/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationWithShortEventDto> getAllCompilationsPublic(@RequestParam(required = false) Boolean pinned,
                                                                       @RequestParam(defaultValue = "0") int from,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return compilationService.getAllCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationWithShortEventDto getCompilationByIdPublic(@PathVariable long compId) {
        return compilationService.getCompilationByIdPublic(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationWithShortEventDto saveCompilationAdmin(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.saveCompilationAdmin(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationWithShortEventDto patchCompilationAdmin(@Valid @RequestBody UpdateCompilationDto newCompilationDto,
                                                        @PathVariable long compId) {
        return compilationService.patchCompilationAdmin(newCompilationDto, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationAdmin(@PathVariable long compId) {
        compilationService.deleteCompilationAdmin(compId);
    }

}
