package ru.practicum.ewm.compilation.controller;

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
    public List<CompilationWithShortEventDto> getAllCompilationsPublic(@RequestParam(required = false) Boolean pinned,
                                                                       @RequestParam(defaultValue = "0") int from,
                                                                       @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(compilationService.getAllCompilationsPublic(pinned, from, size));
        return compilationService.getAllCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationWithShortEventDto getCompilationByIdPublic(@PathVariable long compId) {
        return compilationService.getCompilationByIdPublic(compId);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(compilationService.getCompilationByIdPublic(compId));
    }

    @PostMapping("/admin/compilations")
    public CompilationWithShortEventDto saveCompilationAdmin(@Valid @RequestBody NewCompilationDto newCompilationDto) {
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(compilationService.saveCompilationAdmin(newCompilationDto));
        return compilationService.saveCompilationAdmin(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationWithShortEventDto patchCompilationAdmin(@Valid @RequestBody UpdateCompilationDto newCompilationDto,
                                                        @PathVariable long compId) {
        return compilationService.patchCompilationAdmin(newCompilationDto, compId);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(compilationService.patchCompilationAdmin(newCompilationDto, compId));
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilationAdmin(@PathVariable long compId) {
        compilationService.deleteCompilationAdmin(compId);
        //return ResponseEntity.noContent().build();
    }

}
