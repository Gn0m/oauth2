package com.example.oauth2.service;

import com.example.oauth2.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> findAllUsers();

    void delete(long id);
}
