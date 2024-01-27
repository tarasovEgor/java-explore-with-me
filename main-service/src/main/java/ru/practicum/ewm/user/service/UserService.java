package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserService {

    //ResponseEntity<Object> saveUser(UserDto userDto);

    User saveUser(UserDto userDto);

    //ResponseEntity<Object> getUsersById(long[] ids, int from, int size);

    List<User> getUsersById(long[] ids, int from, int size);

    User deleteUser(long userId);
}
