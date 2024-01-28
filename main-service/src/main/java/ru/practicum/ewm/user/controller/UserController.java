package ru.practicum.ewm.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/users")
    public User saveUser(@Valid @RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(userService.saveUser(userDto));
    }

    @GetMapping("/admin/users")
    public List<User> getUsersById(@RequestParam(required = false) long[] ids,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size) {
        return userService.getUsersById(ids, from, size);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(userService.getUsersById(ids, from, size));
    }

    @DeleteMapping("/admin/users/{userId}")
    public User deleteUser(@PathVariable long userId) {
        return userService.deleteUser(userId);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT)
//                .body(userService.deleteUser(userId));
    }

}
