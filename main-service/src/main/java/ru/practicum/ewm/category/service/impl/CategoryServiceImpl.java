package ru.practicum.ewm.category.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.exception.InvalidMethodException;
import ru.practicum.ewm.exception.category.CategoryDoesNotExistException;

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
        Category category = CategoryMapper.toCategory(newCategoryDto);

        try {

//            return ResponseEntity
//                    .status(HttpStatus.CREATED)
//                    .body(repository.save(category));
            return repository.save(category);

        } catch (RuntimeException e) {

//            return ResponseEntity
//                    .status(HttpStatus.CONFLICT)
//                    .body(new ApiError(
//                            "409",
//                            "Conflict.",
//                            "Invalid method."
//                    ));
            throw new InvalidMethodException("Method not allowed.");

        }
    }

    @Override
    public Category getCategoryById(long categoryId) {
        Optional<Category> category = repository.findById(categoryId);

        if (category.isEmpty()) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError());

            throw new CategoryDoesNotExistException("Category doesn't exist.");
        }

        return category.get();
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(category);

//        try {
//            return category.get();
//        } catch (RuntimeException e) {
//            throw new CategoryDoesNotExistException("Category doesn't exist.");
//
//        }
    }

    @Override
    public List<Category> getAllCategories(int from, int size) {
        Page<Category> page = repository.findAll(PageRequest.of(from, size));
        return page.getContent();
    }

    @Override
    public Category patchCategory(long categoryId, NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        category.setId(categoryId);

        try {

            repository.update(category.getName(), categoryId);
            category = repository.findById(categoryId).get();
            return category;


//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(category);

        } catch (RuntimeException e) {

            throw new InvalidMethodException("Method not allowed.");
//            return ResponseEntity
//                    .status(HttpStatus.CONFLICT)
//                    .body(new ApiError(
//                            "409",
//                            "Conflict.",
//                            "Invalid method."
//                    ));
        }
    }

    @Override
    public void deleteCategory(long categoryId) {
        Optional<Category> category = repository.findById(categoryId);

        if (category.isPresent() && category.get().getClass().equals(Category.class)) {

            try {

                repository.delete(category.get());
                log.info("Successfully deleted category with the id of - '{}'", categoryId);

//                return ResponseEntity
//                        .noContent()
//                        .build();

            } catch (RuntimeException e) {

                throw new InvalidMethodException("Method not allowed.");
//                return ResponseEntity
//                        .status(HttpStatus.CONFLICT)
//                        .body(new ApiError(
//                                "409",
//                                "Conflict.",
//                                "Method not allowed."
//                        ));
            }

        } else {

            throw new CategoryDoesNotExistException("Category doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "Category doesn't exist."
//                    ));
        }
    }

}
