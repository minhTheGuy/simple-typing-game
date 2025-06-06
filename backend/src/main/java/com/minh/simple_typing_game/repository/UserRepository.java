package com.minh.simple_typing_game.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.minh.simple_typing_game.entity.User;
import com.minh.simple_typing_game.entity.enums.AuthProvider;

/**
 * Repository interface for User entity.
 * Provides methods to perform CRUD operations and custom queries.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {    /**
     * Finds a User by their username.
     *
     * @param username the username of the user
     * @return the User entity if found, otherwise null
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a User by their email.
     *
     * @param email the email of the user
     * @return the User entity if found, otherwise null
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a User by their provider ID and provider name.
     *
     * @param providerId the provider ID of the user
     * @param provider   the OAuth provider enum
     * @return the User entity if found, otherwise null
     */
    Optional<User> findByProviderIdAndProvider(String providerId, AuthProvider provider);

    /**
     * Finds a User by email or provider combination.
     *
     * @param email the email to search for
     * @param providerId the provider ID to search for
     * @return the User entity if found, otherwise empty
     */
    @Query("SELECT u FROM User u WHERE u.email = :email OR (u.providerId = :providerId AND u.provider = :provider)")
    Optional<User> findByEmailOrProviderInfo(@Param("email") String email, 
                                           @Param("providerId") String providerId, 
                                           @Param("provider") AuthProvider provider);
    
    /**
     * Checks if a User exists by their username.
     *
     * @param username the username to check
     * @return true if a user with the given username exists, otherwise false
     */
    boolean existsByUsername(String username);
    
    /**
     * Checks if a User exists by their email.
     *
     * @param email the email to check
     * @return true if a user with the given email exists, otherwise false
     */
    boolean existsByEmail(String email);    /**
     * Checks if a User exists by their provider ID and provider name.
     *
     * @param providerId the provider ID to check
     * @param provider   the OAuth provider enum
     * @return true if a user with the given provider ID and provider name exists, otherwise false
     */
    boolean existsByProviderIdAndProvider(String providerId, AuthProvider provider);

}