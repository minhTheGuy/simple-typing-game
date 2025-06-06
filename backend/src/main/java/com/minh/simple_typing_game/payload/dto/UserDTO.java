package com.minh.simple_typing_game.payload.dto;

import java.time.LocalDateTime;

import com.minh.simple_typing_game.entity.enums.AuthProvider;
import com.minh.simple_typing_game.entity.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;
    private String providerId;
    private AuthProvider provider;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Game statistics - these can be added to User entity later
    private Integer bestWpm;
    private Integer totalGames;
    private Integer totalWords;
}
