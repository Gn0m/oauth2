package com.example.oauth2.service.impl;

import com.example.oauth2.dto.UserDTO;
import com.example.oauth2.entity.User;
import com.example.oauth2.enums.Role;
import com.example.oauth2.mapper.UserMapper;
import com.example.oauth2.repository.UserRepository;
import com.example.oauth2.service.UserAttemptsService;
import com.example.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final UserAttemptsService attemptsService;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User userToSave = mapper.convertToEntity(userDTO);
        userToSave.setPassword(encoder.encode(userToSave.getPassword()));
        userToSave.getRoles().add(Role.USER);
        userToSave.setAccountNonLocked(true);
        User savedUser = repository.save(userToSave);

        attemptsService.create(userDTO.getUserName());
        return mapper.convertToDto(savedUser);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        User user = repository.findById(String.valueOf(id)).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь не найден"));
        repository.delete(user);
        attemptsService.delete(user.getUserName());
    }
}











