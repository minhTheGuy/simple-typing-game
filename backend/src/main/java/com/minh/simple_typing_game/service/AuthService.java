package com.minh.simple_typing_game.service;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.minh.simple_typing_game.entity.User;

public interface AuthService {

    /**
     * Logs in a user with the provided username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return a JWT token if login is successful, null otherwise
     */
    String login(String username, String password);

    /**
     * Registers a new user with the provided username, password, and email.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param email the email of the new user
     * @return a JWT token if registration is successful, null otherwise
     */
    String register(String username, String password, String email);

    /**
     * Refreshes the JWT token using the provided refresh token.
     *
     * @param refreshToken the refresh token
     * @return a new JWT token if the refresh is successful, null otherwise
     */
    String refreshToken(String refreshToken);

    /**
     * Logs out the user by invalidating the provided token.
     *
     * @param token the JWT token to invalidate
     */
    void logout(String token);

    /**
     * Validates the provided JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param token the JWT token
     * @return the username if extraction is successful, null otherwise
     */
    String getUsernameFromToken(String token);

    /**
     * Extracts the email from the provided JWT token.
     *
     * @param token the JWT token
     * @return the email if extraction is successful, null otherwise
     */
    String getEmailFromToken(String token);

    /**
     * Creates or updates an OAuth2 user in the database.
     *
     * @param oauth2User the OAuth2 user data from the provider
     * @param provider the OAuth provider name (GOOGLE, GITHUB)
     * @return the created or updated User entity
     */
    User createOrUpdateOAuth2User(OAuth2User oauth2User, String provider);

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return the User entity if found, null otherwise
     */
    User findByEmail(String email);

    /**
     * Generates a JWT token for OAuth2 authenticated user.
     *
     * @param user the authenticated user
     * @return JWT token string
     */
    String generateTokenForOAuth2User(User user);
}
