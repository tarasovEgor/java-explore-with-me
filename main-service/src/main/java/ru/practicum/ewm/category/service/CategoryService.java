package ru.practicum.ewm.category.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryService {

    Category saveCategory(NewCategoryDto newCategoryDto);

    ResponseEntity<Object> getCategoryById(long categoryId);

    List<Category> getAllCategories(int from, int size);

    Category updateCategory(long categoryId, NewCategoryDto newCategoryDto);

    ResponseEntity<Object> deleteCategory(long categoryId);


}
