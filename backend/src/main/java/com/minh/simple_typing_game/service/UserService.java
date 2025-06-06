package com.minh.simple_typing_game.service;

import java.util.List;

import com.minh.simple_typing_game.payload.dto.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByEmail(String email);
}