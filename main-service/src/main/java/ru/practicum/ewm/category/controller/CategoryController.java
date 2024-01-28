package ru.practicum.ewm.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    public Category saveCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.saveCategory(newCategoryDto);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(categoryService.saveCategory(newCategoryDto));
    }

    @GetMapping("/categories/{categoryId}")
    public Category getCategoryById(@PathVariable long categoryId) {
        return categoryService.getCategoryById(categoryId);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(categoryService.getCategoryById(categoryId));
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAllCategories(from, size);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(categoryService.getAllCategories(from, size));
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public Category updateCategory(@PathVariable long categoryId,
                                         @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.patchCategory(categoryId, newCategoryDto);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(categoryService.patchCategory(categoryId, newCategoryDto));
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public void deleteCategory(@PathVariable long categoryId) {
        categoryService.deleteCategory(categoryId);
        //return ResponseEntity.noContent().build();
    }

}
