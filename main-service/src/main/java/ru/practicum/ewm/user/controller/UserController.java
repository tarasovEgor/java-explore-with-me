package ru.practicum.ewm.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/users")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.saveUser(userDto));
    }

    @GetMapping("/admin/users")
    public ResponseEntity<?> getUsersById(@RequestParam(required = false) long[] ids,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUsersById(ids, from, size));
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(userService.deleteUser(userId));
    }

}
