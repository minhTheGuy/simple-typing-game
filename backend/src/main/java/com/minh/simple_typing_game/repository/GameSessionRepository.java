package com.minh.simple_typing_game.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.minh.simple_typing_game.entity.GameSession;
import com.minh.simple_typing_game.entity.enums.GameStatus;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    List<GameSession> findByUserIdOrderByCompletedAtDesc(Long userId);

    @Query("SELECT AVG(g.wpm) FROM GameSession g WHERE g.user.id = :userId AND g.status = 'COMPLETED'")
    Double findAverageWpmByUserId(@Param("userId") Long userId);

    List<GameSession> findTop10ByStatusOrderByWpmDesc(GameStatus status);
    
    // Find active game session for a user
    Optional<GameSession> findByUserIdAndStatus(Long userId, GameStatus status);
    
    // Find all sessions for a user with specific status
    List<GameSession> findByUserIdAndStatusOrderByStartedAtDesc(Long userId, GameStatus status);
    
    // Count total games for a user
    long countByUserIdAndStatus(Long userId, GameStatus status);
}
