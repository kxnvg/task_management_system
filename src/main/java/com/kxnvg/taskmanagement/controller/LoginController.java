package com.kxnvg.taskmanagement.controller;

import com.kxnvg.taskmanagement.dto.AuthenticateRequestDto;
import com.kxnvg.taskmanagement.dto.AuthenticateResponseDto;
import com.kxnvg.taskmanagement.dto.RegisterRequestDto;
import com.kxnvg.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/register")
    public AuthenticateResponseDto registerUser(@RequestBody RegisterRequestDto requestDto) {
        log.info("Received request to register a new user with email={}", requestDto.getEmail());
        return userService.registerUser(requestDto);
    }

    @PostMapping("/authenticate")
    public AuthenticateResponseDto createAuthToken(@RequestBody AuthenticateRequestDto requestDto) {
        log.info("Received request to authenticate user with email={}", requestDto.getEmail());
        return userService.authenticateUser(requestDto);
    }
}
