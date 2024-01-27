package ru.practicum.ewm.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<?> saveCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.saveCategory(newCategoryDto));
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable long categoryId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoryById(categoryId));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getAllCategories(from, size));
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable long categoryId,
                                                 @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.patchCategory(categoryId, newCategoryDto));
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
