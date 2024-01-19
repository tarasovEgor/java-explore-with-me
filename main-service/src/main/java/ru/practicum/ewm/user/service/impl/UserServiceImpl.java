package ru.practicum.ewm.user.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User saveUser(UserDto userDto) {
        /*
        *   CHECKS !!!
        * */
        User user = UserMapper.toUser(userDto);
        return repository.save(user);
    }

    @Override
    public List<User> getUsersById(long[] ids, int from, int size) {
        /*
        *   CHECKS !!!
        * */
        Page<User> page = repository.findAll(PageRequest.of(from, size));
        if (ids.length != 0) {
            page = repository.findAllByIdIn(ids, PageRequest.of(from, size));
            return page.getContent();
        } else {
            return page.getContent();
        }
    }

    @Override
    public User deleteUser(long userId) {
        /*
        *   CHECKS !!!
        * */
        User user = repository.findById(userId).get();
        repository.delete(user);
        log.info("Successfully deleted user with the id of - {}", userId);
        return user;
    }


}
