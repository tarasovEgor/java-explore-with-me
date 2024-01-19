package ru.practicum.ewm.category.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.error.ApiError;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category saveCategory(NewCategoryDto newCategoryDto) {
        /*
        *   CHECKS !!!
        * */
        Category category = CategoryMapper.toCategory(newCategoryDto);
        return repository.save(category);
    }

    @Override
    public ResponseEntity<Object> getCategoryById(long categoryId) {
        /*
        *   CHECKS !!!
        * */
        Optional<Category> category = repository.findById(categoryId);
        if (category.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError());
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @Override
    public List<Category> getAllCategories(int from, int size) {
        /*
        *   CHECKS !!!
        * */
        Page<Category> page = repository.findAll(PageRequest.of(from, size));
        return page.getContent();
    }

    @Override
    public Category updateCategory(long categoryId, NewCategoryDto newCategoryDto) {
        /*
        *   CHECKS !!!
        * */
        Category category = CategoryMapper.toCategory(newCategoryDto);
        category.setId(categoryId);
        repository.update(category.getName(), categoryId);
        return category;
    }

    @Override
    public ResponseEntity<Object> deleteCategory(long categoryId) {
        /*
        *   CHECKS !!!
        * */
        Optional<Category> category = repository.findById(categoryId);
        if (category.isEmpty()) return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiError()
        );
        repository.delete(category.get());
        log.info("Successfully deleted category with the id of - '{}'", categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(category);
    }


}
