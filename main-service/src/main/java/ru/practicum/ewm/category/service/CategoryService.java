package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryService {

    Category saveCategory(NewCategoryDto newCategoryDto);

    Category getCategoryById(long categoryId);

    List<Category> getAllCategories(int from, int size);

    Category patchCategory(long categoryId, NewCategoryDto newCategoryDto);

     void deleteCategory(long categoryId);

}
