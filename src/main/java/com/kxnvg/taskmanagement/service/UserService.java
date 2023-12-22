package com.kxnvg.taskmanagement.service;

import com.kxnvg.taskmanagement.dto.AuthenticateRequestDto;
import com.kxnvg.taskmanagement.dto.AuthenticateResponseDto;
import com.kxnvg.taskmanagement.dto.RegisterRequestDto;
import com.kxnvg.taskmanagement.dto.UserDto;
import com.kxnvg.taskmanagement.entity.User;
import com.kxnvg.taskmanagement.entity.enums.UserRole;
import com.kxnvg.taskmanagement.mapper.UserMapper;
import com.kxnvg.taskmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
       User user = takeUserFromDB(userId);
       log.info("User with id={} was taken from DB successfully", userId);
       return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        User user = takeUserFromDB(userDto.getId());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        log.info("User with id={} is updated successfully", userDto.getId());
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUser() {
        List<User> allUsers = userRepository.findAll();
        log.info("All users was taken from DB successfully");
        return allUsers.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            log.info("User with id={} was deleted from DB successfully", userId);
            return true;
        }
        log.info("User with id={} is not found in DB", userId);
        return false;
    }

    @Transactional
    public AuthenticateResponseDto registerUser(RegisterRequestDto requestDto) {
        User user = User.builder()
                .firstname(requestDto.getFirstname())
                .lastname(requestDto.getLastname())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(requestDto.getRole())
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
        log.info("User with email={} was successfully authenticated", user.getEmail());

        String jwtToken = jwtTokenService.generateToken(user);
        return AuthenticateResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    protected User takeUserFromDB(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%d is not found", userId)));
    }
}
