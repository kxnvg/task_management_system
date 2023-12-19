package com.kxnvg.taskmanagement.service;

import com.kxnvg.taskmanagement.dto.AuthenticateRequestDto;
import com.kxnvg.taskmanagement.dto.AuthenticateResponseDto;
import com.kxnvg.taskmanagement.dto.RegisterRequestDto;
import com.kxnvg.taskmanagement.dto.UserDto;
import com.kxnvg.taskmanagement.entity.User;
import com.kxnvg.taskmanagement.entity.enums.UserRole;
import com.kxnvg.taskmanagement.mapper.UserMapper;
import com.kxnvg.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        return null;
    }

    @Transactional
    public AuthenticateResponseDto registerUser(RegisterRequestDto requestDto) {
        User user = User.builder()
                .firstname(requestDto.getFirstname())
                .lastname(requestDto.getLastname())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(UserRole.USER)
                .comments(new ArrayList<>())
                .createdTasks(new ArrayList<>())
                .executableTasks(new ArrayList<>())
                .build();
        userRepository.save(user);
        log.info("New user with email={} was saved in DB successfully", user.getEmail());

        String jwtToken = jwtTokenService.generateToken(user);
        return AuthenticateResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional(readOnly = true)
    public AuthenticateResponseDto authenticateUser(AuthenticateRequestDto requestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword())
        );
        var user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() ->  new UsernameNotFoundException(
                        String.format("User with email=%s is not found in DB", requestDto.getEmail())));
        log.info("User with email={} was take from DB successfully", user.getEmail());

        String jwtToken = jwtTokenService.generateToken(user);
        return AuthenticateResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username=%s is not found in DB", username)));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );
    }
}
