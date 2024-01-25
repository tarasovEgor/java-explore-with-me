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
    public ResponseEntity<Object> saveCategory(NewCategoryDto newCategoryDto) {

        Category category = CategoryMapper.toCategory(newCategoryDto);

        try {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(repository.save(category));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiError(
                            "409",
                            "Conflict.",
                            "Invalid method."
                    ));

        }

    }

    @Override
    public ResponseEntity<Object> getCategoryById(long categoryId) {

        Optional<Category> category = repository.findById(categoryId);

        if (category.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(category);

    }

    @Override
    public List<Category> getAllCategories(int from, int size) {
        Page<Category> page = repository.findAll(PageRequest.of(from, size));
        return page.getContent();
    }

    @Override
    public ResponseEntity<Object> patchCategory(long categoryId, NewCategoryDto newCategoryDto) {

        Category category = CategoryMapper.toCategory(newCategoryDto);
        category.setId(categoryId);

        try {

            repository.update(category.getName(), categoryId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(category);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiError(
                            "409",
                            "Conflict.",
                            "Invalid method."
                    ));
        }
    }

    @Override
    public ResponseEntity<Object> deleteCategory(long categoryId) {

        Optional<Category> category = repository.findById(categoryId);

        if (category.isPresent() && category.get().getClass().equals(Category.class)) {

            try {

                repository.delete(category.get());
                log.info("Successfully deleted category with the id of - '{}'", categoryId);

                return ResponseEntity
                        .noContent()
                        .build();

            } catch (RuntimeException e) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new ApiError(
                                "409",
                                "Conflict.",
                                "Method not allowed."
                        ));
            }

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "Category doesn't exist."
                    ));
        }

    }


}
