package ru.practicum.ewm.compilation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping("/compilations")
    public ResponseEntity<?> getAllCompilationsPublic(@RequestParam(required = false) Boolean pinned,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationService.getAllCompilationsPublic(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<?> getCompilationByIdPublic(@PathVariable long compId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationService.getCompilationByIdPublic(compId));
    }

    @PostMapping("/admin/compilations")
    public ResponseEntity<?> saveCompilationAdmin(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compilationService.saveCompilationAdmin(newCompilationDto));
    }

    @PatchMapping("/admin/compilations/{compId}")
    public ResponseEntity<?> patchCompilationAdmin(@Valid @RequestBody UpdateCompilationDto newCompilationDto,
                                                        @PathVariable long compId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationService.patchCompilationAdmin(newCompilationDto, compId));
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public ResponseEntity<?> deleteCompilationAdmin(@PathVariable long compId) {
        compilationService.deleteCompilationAdmin(compId);
        return ResponseEntity.noContent().build();
    }

}
