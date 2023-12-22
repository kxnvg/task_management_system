package com.kxnvg.taskmanagement.controller;

import com.kxnvg.taskmanagement.dto.UserDto;
import com.kxnvg.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Received request to get user with id={}", userId);
        return userService.getUser(userId);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        log.info("Received request to update user with id={}", userDto.getId());
        return userService.updateUser(userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Received request to get all users");
        return userService.getAllUser();
    }

    @DeleteMapping("/{userId}")
    public Boolean deleteUser(@PathVariable Long userId) {
        log.info("Received request to delete user with id={}", userId);
        return userService.deleteUser(userId);
    }
}
