package ru.practicum.ewm.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<Category> saveCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.saveCategory(newCategoryDto));
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAllCategories(from, size);
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public Category updateCategory(@PathVariable long categoryId,
                                   @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.updateCategory(categoryId, newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long categoryId) {
//        return ResponseEntity.status(HttpStatus.NO_CONTENT)
//                .body(categoryService.deleteCategory(categoryId));
        return categoryService.deleteCategory(categoryId);
    }

}
