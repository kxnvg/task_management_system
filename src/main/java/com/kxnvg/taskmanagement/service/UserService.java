package com.kxnvg.taskmanagement.service;

import com.kxnvg.taskmanagement.dto.UserDto;
import com.kxnvg.taskmanagement.entity.User;
import com.kxnvg.taskmanagement.entity.UserRole;
import com.kxnvg.taskmanagement.mapper.UserMapper;
import com.kxnvg.taskmanagement.repository.UserRepository;
import com.kxnvg.taskmanagement.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with username=%s is not found in DB", username)));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }

    @Transactional
    public void createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setRoles(Set.of(roleRepository.findByName("USER").get()));
        userRepository.save(user);
    }
}
