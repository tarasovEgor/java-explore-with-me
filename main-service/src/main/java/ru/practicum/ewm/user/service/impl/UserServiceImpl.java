package ru.practicum.ewm.user.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.exception.InvalidMethodException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public User saveUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        try {
            return userRepository.save(user);
        } catch (RuntimeException e) {
            throw new InvalidMethodException("Method not allowed.");
        }
    }

    @Override
    public List<User> getUsersById(long[] ids, int from, int size) {
        Page<User> page = userRepository.findAll(PageRequest.of(from, size));
        if (ids == null) {
            return page.getContent();
        } else {
            page = userRepository.findAllByIdIn(ids, PageRequest.of(from, size));
            return page.getContent();
        }
    }

    @Override
    public User deleteUser(long userId) {
        User user = userRepository.findById(userId).get();
        userRepository.delete(user);
        log.info("Successfully deleted user with the id of - {}", userId);
        return user;
    }

}
