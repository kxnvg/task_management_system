package com.kxnvg.taskmanagement.service;

import com.kxnvg.taskmanagement.client.UserContext;
import com.kxnvg.taskmanagement.dto.UserDto;
import com.kxnvg.taskmanagement.entity.User;
import com.kxnvg.taskmanagement.entity.enums.UserRole;
import com.kxnvg.taskmanagement.exception.IncorrectUserActionException;
import com.kxnvg.taskmanagement.mapper.UserMapperImpl;
import com.kxnvg.taskmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final long USER_ID = 1L;
    private User user;
    private UserDto userDto;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserContext userContext;

    @Spy
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void initData() {
        user = User.builder()
                .id(USER_ID)
                .email("test@mail.ru")
                .password("test")
                .createdTasks(new ArrayList<>())
                .executableTasks(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        userDto = UserDto.builder()
                .id(USER_ID)
                .email("test@mail.ru")
                .password("test")
                .createdTasksId(new ArrayList<>())
                .executableTasksId(new ArrayList<>())
                .commentsId(new ArrayList<>())
                .build();
    }

    @Test
    void testGetUserWithoutEntityInDB() {
        when(userRepository.findById(USER_ID)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(USER_ID));
    }

    @Test
    void testGetUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        UserDto actualUserDto = userService.getUser(USER_ID);
        assertEquals(userDto, actualUserDto);
    }

    @Test
    void testUpdateUserWithIncorrectUserAction() {
        user.setRole(UserRole.USER);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user));
        when(userContext.getUserId()).thenReturn(2L);
        assertThrows(IncorrectUserActionException.class, () -> userService.updateUser(userDto));
    }

    @Test
    void testUpdateUser() {
        user.setRole(UserRole.ADMIN);
        userDto.setRole(UserRole.ADMIN);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user));
        when(userContext.getUserId()).thenReturn(2L);

        UserDto actualUserDto = userService.updateUser(userDto);
        assertEquals(userDto, actualUserDto);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDto> actualList = userService.getAllUsers();
        List<UserDto> expectedList = List.of(userDto);
        assertEquals(expectedList, actualList);
    }

    @Test
    void testDeleteUserWithoutEntity() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        when(userContext.getUserId()).thenReturn(USER_ID);
        boolean actualResult = userService.deleteUser(USER_ID);
        assertEquals(false, actualResult);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        when(userContext.getUserId()).thenReturn(USER_ID);
        boolean actualResult = userService.deleteUser(USER_ID);
        assertEquals(true, actualResult);
    }

    @Test
    void testMakeAdminRoleToAdmin() {
        user.setRole(UserRole.ADMIN);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        boolean actualResult = userService.makeAdminRole(USER_ID);
        assertEquals(false, actualResult);
    }

    @Test
    void testMakeAdminRole() {
        user.setRole(UserRole.USER);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));

        boolean actualResult = userService.makeAdminRole(USER_ID);
        assertEquals(true, actualResult);
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void testMakeUserRoleToUser() {
        user.setRole(UserRole.USER);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        boolean actualResult = userService.makeUserRole(USER_ID);
        assertEquals(false, actualResult);
    }

    @Test
    void testMakeUserRole() {
        user.setRole(UserRole.ADMIN);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));

        boolean actualResult = userService.makeUserRole(USER_ID);
        assertEquals(true, actualResult);
        assertEquals(UserRole.USER, user.getRole());
    }
}