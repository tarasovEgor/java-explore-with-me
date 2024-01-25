package ru.practicum.ewm.user.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.error.ApiError;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<Object> saveUser(UserDto userDto) {

        User user = UserMapper.toUser(userDto);

        try {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(repository.save(user));

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiError(
                            "409",
                            "Conflict.",
                            "MEthod not allowed."
                    ));
        }

    }

    @Override
    public ResponseEntity<Object> getUsersById(long[] ids, int from, int size) {

        Page<User> page = repository.findAll(PageRequest.of(from, size));
        if (ids == null) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(page.getContent());

        } else {

            page = repository.findAllByIdIn(ids, PageRequest.of(from, size));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(page.getContent());
        }

    }

    @Override
    public User deleteUser(long userId) {
        User user = repository.findById(userId).get();
        repository.delete(user);
        log.info("Successfully deleted user with the id of - {}", userId);
        return user;
    }

}
