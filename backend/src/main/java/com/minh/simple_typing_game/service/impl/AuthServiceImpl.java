package com.minh.simple_typing_game.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.minh.simple_typing_game.entity.User;
import com.minh.simple_typing_game.entity.enums.AuthProvider;
import com.minh.simple_typing_game.entity.enums.Role;
import com.minh.simple_typing_game.repository.UserRepository;
import com.minh.simple_typing_game.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* * 
 * An implementation of the AuthService interface.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        return null;
    }    @Override
    public User createOrUpdateOAuth2User(OAuth2User oauth2User, String provider) {
        log.debug("Creating/updating OAuth2 user for provider: {}", provider);
        log.debug("OAuth2 user attributes: {}", oauth2User.getAttributes());
        
        // Extract user information based on provider
        String email = oauth2User.getAttribute("email");
        String providerId = null;
        
        // Extract provider ID based on the specific provider
        switch (provider) {
            case "GOOGLE":
                providerId = oauth2User.getAttribute("sub"); // Google uses 'sub' for user ID
                break;
            case "GITHUB":
                Object idAttribute = oauth2User.getAttribute("id");
                providerId = idAttribute != null ? idAttribute.toString() : null;
                break;
            default:
                Object defaultIdAttribute = oauth2User.getAttribute("id");
                providerId = defaultIdAttribute != null ? defaultIdAttribute.toString() : null;
                break;        }
        
        log.debug("Extracted email: {}, providerId: {}", email, providerId);
        
        // Provider ID is always required
        if (providerId == null) {
            throw new IllegalArgumentException("Provider ID is required for OAuth2 user. Provider ID: " + providerId + ", Provider: " + provider);
        }
        
        // Handle missing email for GitHub users (common when email is private)
        if (email == null && "GITHUB".equals(provider)) {
            String login = oauth2User.getAttribute("login"); // GitHub username
            if (login != null) {
                email = login + "@github.local"; // Generate a placeholder email
                log.info("GitHub user {} has private email, using placeholder: {}", login, email);
            } else {
                email = "user" + providerId + "@github.local"; // Fallback to provider ID
                log.info("GitHub user has no login/email, using fallback: {}", email);
            }
        }
        
        // Email is still required after fallback generation
        if (email == null) {
            throw new IllegalArgumentException("Email could not be determined for OAuth2 user. Provider ID: " + providerId + ", Provider: " + provider);
        }
        
        String firstName;
        String lastName;
        String imageUrl;
        
        // Handle provider-specific attributes
        switch (provider) {
            case "GOOGLE":
                firstName = oauth2User.getAttribute("given_name");
                lastName = oauth2User.getAttribute("family_name");
                imageUrl = oauth2User.getAttribute("picture");
                break;
            case "GITHUB":
                String name = oauth2User.getAttribute("name");
                if (name != null && name.contains(" ")) {
                    String[] nameParts = name.split(" ", 2);
                    firstName = nameParts[0];
                    lastName = nameParts[1];
                } else {
                    firstName = name;
                    lastName = null;
                }
                imageUrl = oauth2User.getAttribute("avatar_url");
                break;
            default:
                firstName = oauth2User.getAttribute("name");
                lastName = null;
                imageUrl = null;
                break;
        }
          AuthProvider authProvider = AuthProvider.valueOf(provider);
        
        // Check if user already exists by provider info first (more reliable for OAuth2)
        User existingUser = userRepository.findByProviderIdAndProvider(providerId, authProvider).orElse(null);
        
        // If not found by provider info, try by email (but skip for generated GitHub emails)
        if (existingUser == null && !email.endsWith("@github.local")) {
            existingUser = userRepository.findByEmail(email).orElse(null);
        }
          if (existingUser != null) {
            // Update existing user
            log.info("Updating existing OAuth2 user: {} (ID: {})", existingUser.getEmail(), existingUser.getId());
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setImageUrl(imageUrl);
            
            // If user was found by email but doesn't have OAuth info, add it
            if (existingUser.getProviderId() == null) {
                log.info("Adding OAuth2 provider info to existing user: {}", existingUser.getEmail());
                existingUser.setProviderId(providerId);
                existingUser.setProvider(authProvider);
            }
            
            User savedUser = userRepository.save(existingUser);
            log.info("Successfully updated OAuth2 user: {}", savedUser.getEmail());
            return savedUser;
        } else {
            // Create new OAuth user
            log.info("Creating new OAuth2 user with email: {}, provider: {}, providerId: {}", email, provider, providerId);
            User newUser = User.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .providerId(providerId)
                    .provider(authProvider)
                    .imageUrl(imageUrl)
                    .role(Role.ROLE_USER)
                    .build();
            
            User savedUser = userRepository.save(newUser);
            log.info("Successfully created new OAuth2 user: {} (ID: {})", savedUser.getEmail(), savedUser.getId());
            return savedUser;
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public String generateTokenForOAuth2User(User user) {
        // For now, return a simple token (you can implement JWT later)
        return "oauth2_token_" + user.getId() + "_" + System.currentTimeMillis();
    }

    @Override
    public String register(String username, String password, String email) {
        // Check if the username or email already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create a new user and save it to the repository
        User user = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password)) // Hash the password
                .email(email)
                .provider(AuthProvider.LOCAL) // Set as local user
                .role(Role.ROLE_USER) // Default role
                .build();

        userRepository.save(user);
        // Return a token or some identifier for the newly registered user

        return "Registration successful"; // Placeholder, should return a JWT or similar token
    }

    @Override
    public String refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return null;
    }

    @Override
    public String getEmailFromToken(String token) {
        return null;
    }
}
