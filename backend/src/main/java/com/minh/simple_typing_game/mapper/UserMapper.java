package com.minh.simple_typing_game.mapper;

import org.springframework.stereotype.Component;

import com.minh.simple_typing_game.entity.User;
import com.minh.simple_typing_game.payload.dto.UserDTO;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setImageUrl(user.getImageUrl());
        userDTO.setProvider(user.getProvider());
        userDTO.setProviderId(user.getProviderId());
        userDTO.setRole(user.getRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());

        // Set default values for game statistics (these fields don't exist in User entity yet)
        userDTO.setBestWpm(0);
        userDTO.setTotalGames(0);
        userDTO.setTotalWords(0);

        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .imageUrl(userDTO.getImageUrl())
                .provider(userDTO.getProvider())
                .providerId(userDTO.getProviderId())
                .role(userDTO.getRole())
                .build();
    }

}
