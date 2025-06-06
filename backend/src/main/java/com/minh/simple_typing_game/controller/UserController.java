package com.minh.simple_typing_game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minh.simple_typing_game.payload.dto.UserDTO;
import com.minh.simple_typing_game.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        if (id == null || id <= 0) {
            log.error("Invalid user ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            log.warn("User with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
        if (id == null || id <= 0) {
            log.error("Invalid user ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            if (updatedUser == null) {
                log.warn("User with ID {} not found for update", id);
                return ResponseEntity.notFound().build();
            }
            
            log.info("Successfully updated user with ID: {}", id);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error updating user with ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable Long id) {
        log.info("Fetching stats for user with ID: {}", id);
        if (id == null || id <= 0) {
            log.error("Invalid user ID for stats: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        // For now, return basic user stats - you can expand this later
        try {
            UserDTO user = userService.getUserById(id);
            if (user == null) {
                log.warn("User with ID {} not found for stats", id);
                return ResponseEntity.notFound().build();
            }
            
            // Return basic stats (you can create a separate UserStatsDTO later)
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error fetching stats for user {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
