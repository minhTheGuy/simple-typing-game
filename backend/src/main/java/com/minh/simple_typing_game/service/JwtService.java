package com.minh.simple_typing_game.service;

import com.minh.simple_typing_game.entity.User;

/**
 * Service interface for JWT token operations.
 */
public interface JwtService {
    
    /**
     * Generates a JWT token for the given user.
     *
     * @param user the user for whom to generate the token
     * @return the generated JWT token
     */
    String generateToken(User user);
    
    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID
     */
    Long getUserIdFromToken(String token);
    
    /**
     * Extracts the email from the JWT token.
     *
     * @param token the JWT token
     * @return the email
     */
    String getEmailFromToken(String token);
    
    /**
     * Validates the JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
    
    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    boolean isTokenExpired(String token);
}
