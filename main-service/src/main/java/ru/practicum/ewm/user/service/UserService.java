package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserService {

    User saveUser(UserDto userDto);

    List<User> getUsersById(long[] ids, int from, int size);

    User deleteUser(long userId);
}
