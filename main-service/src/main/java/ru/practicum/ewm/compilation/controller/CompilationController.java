package ru.practicum.ewm.compilation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

@RestController
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping("/compilations")
    public ResponseEntity<Object> getAllCompilationsPublic(@RequestParam(required = false) Boolean pinned,
                                                           @RequestParam(required = false, defaultValue = "0") int from,
                                                           @RequestParam(required = false, defaultValue = "10") int size) {
        return compilationService.getAllCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<Object> getCompilationByIdPublic(@PathVariable long compId) {
        return compilationService.getCompilationByIdPublic(compId);
    }

    @PostMapping("/admin/compilations")
    public ResponseEntity<Object> saveCompilationAdmin(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.saveCompilationAdmin(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public ResponseEntity<Object> patchCompilationAdmin(@RequestBody NewCompilationDto newCompilationDto,
                                                        @PathVariable long compId) {
        return compilationService.patchCompilationAdmin(newCompilationDto, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilationAdmin(@PathVariable long compId) {
        return compilationService.deleteCompilationAdmin(compId);
    }




}
