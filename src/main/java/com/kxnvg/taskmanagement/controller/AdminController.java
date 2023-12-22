package com.kxnvg.taskmanagement.controller;

import com.kxnvg.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PutMapping("/users/{userId}/make-admin")
    public Boolean makeAdminRole(@PathVariable Long userId) {
        log.info("Received request for ADMIN API to change user's role on admin, user's id={}", userId);
        return userService.makeAdminRole(userId);
    }

    @PutMapping("/users/{userId}/make-user")
    public Boolean makeUserRole(@PathVariable Long userId) {
        log.info("Received request for ADMIN API to change user's role on user, user's id={}", userId);
        return userService.makeUserRole(userId);
    }
}
