package ru.practicum.ewm.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/users")
    public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
        /*
        *   REFACTOR!!!
        * */
        //return userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(userDto));
    }

    @GetMapping("/admin/users")
    public List<User> getUsersById(@RequestParam(required = false) long[] ids,
                                   @RequestParam(required = false, defaultValue = "0") int from,
                                   @RequestParam(required = false, defaultValue = "10") int size) {
        return userService.getUsersById(ids, from, size);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        /*
        *   REFACTOR!!!
        * */
        //return userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(userService.deleteUser(userId));
    }

}