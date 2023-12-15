package com.kxnvg.taskmanagement.controller;

import com.kxnvg.taskmanagement.dto.JwtRequestDto;
import com.kxnvg.taskmanagement.exception.IncorrectAuthenticationException;
import com.kxnvg.taskmanagement.service.JwtTokenService;
import com.kxnvg.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public String createAuthToken(@RequestBody JwtRequestDto jwtRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequestDto.getUsername(), jwtRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IncorrectAuthenticationException("Incorrect login or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtRequestDto.getUsername());
        return jwtTokenService.generateToken(userDetails);
    }
}
