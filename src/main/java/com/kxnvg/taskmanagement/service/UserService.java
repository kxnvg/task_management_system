package com.kxnvg.taskmanagement.service;

import com.kxnvg.taskmanagement.client.UserContext;
import com.kxnvg.taskmanagement.dto.AuthenticateRequestDto;
import com.kxnvg.taskmanagement.dto.AuthenticateResponseDto;
import com.kxnvg.taskmanagement.dto.RegisterRequestDto;
import com.kxnvg.taskmanagement.dto.UserDto;
import com.kxnvg.taskmanagement.entity.User;
import com.kxnvg.taskmanagement.entity.enums.UserRole;
import com.kxnvg.taskmanagement.exception.IncorrectUserActionException;
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
    private final UserContext userContext;

    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
       User user = takeUserFromDB(userId);
       log.info("User with id={} was taken from DB successfully", userId);
       return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        Long userId = userDto.getId();

        User user = checkCorrectnessUserAction(userId);
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        log.info("User with id={} is updated successfully", userId);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        log.info("All users was taken from DB successfully");
        return allUsers.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    public boolean deleteUser(Long userId) {
        try {
            checkCorrectnessUserAction(userId);
            userRepository.deleteById(userId);
            log.info("User with id={} was deleted from DB successfully", userId);
            return true;
        } catch (EntityNotFoundException e) {
            log.info("User with id={} is not found in DB", userId);
            return false;
        }
    }

    @Transactional
    public Boolean makeAdminRole(Long userId) {
        User user = takeUserFromDB(userId);
        if (user.getRole() == UserRole.ADMIN) {
            return false;
        }
        user.setRole(UserRole.ADMIN);
        log.info("User with id={} became an administrator", userId);
        return true;
    }

    @Transactional
    public Boolean makeUserRole(Long userId) {
        User user = takeUserFromDB(userId);
        if (user.getRole() == UserRole.USER) {
            return false;
        }
        user.setRole(UserRole.USER);
        log.info("User with id={} became an user", userId);
        return true;
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

    protected User checkCorrectnessUserAction(Long userId) {
        User requestSender = takeUserFromDB(userContext.getUserId());
        User user = takeUserFromDB(userId);
        if (requestSender.getRole() == UserRole.ADMIN || userContext.getUserId() == userId) {
            return user;
        }
        throw new IncorrectUserActionException("You can't do this operation or you must have ADMIN role");
    }

    protected User takeUserFromDB(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%d is not found", userId)));
    }
}
