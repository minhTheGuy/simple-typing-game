package com.minh.simple_typing_game.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.minh.simple_typing_game.mapper.UserMapper;
import com.minh.simple_typing_game.payload.dto.UserDTO;
import com.minh.simple_typing_game.repository.UserRepository;
import com.minh.simple_typing_game.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository; // Assuming you have a UserRepository for database operations

    private final UserMapper userMapper; // Assuming you have a UserMapper to convert between User and UserDTO

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserDTO> getAllUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElse(null);
    }    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // Update only non-null fields
                    if (userDTO.getFirstName() != null) {
                        existingUser.setFirstName(userDTO.getFirstName());
                    }
                    if (userDTO.getLastName() != null) {
                        existingUser.setLastName(userDTO.getLastName());
                    }
                    if (userDTO.getUsername() != null) {
                        existingUser.setUsername(userDTO.getUsername());
                    }
                    
                    return userMapper.toDTO(userRepository.save(existingUser));
                })
                .orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO)
                .orElse(null);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO)
                .orElse(null);
    }

}