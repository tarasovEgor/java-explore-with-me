package ru.practicum.ewm.category.service;

import org.springframework.http.ResponseEntity;

import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryService {

    //ResponseEntity<Object> saveCategory(NewCategoryDto newCategoryDto);

    Category saveCategory(NewCategoryDto newCategoryDto);

    //ResponseEntity<Object> getCategoryById(long categoryId);

    Category getCategoryById(long categoryId);


    List<Category> getAllCategories(int from, int size);

    //ResponseEntity<Object> patchCategory(long categoryId, NewCategoryDto newCategoryDto);

    Category patchCategory(long categoryId, NewCategoryDto newCategoryDto);

   // ResponseEntity<Object> deleteCategory(long categoryId);

     void deleteCategory(long categoryId);


}
