package ru.practicum.ewm.user.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;


public interface UserService {

    User saveUser(UserDto userDto);

    ResponseEntity<Object> getUsersById(long[] ids, int from, int size);

    User deleteUser(long userId);
}
