package com.minh.simple_typing_game.entity;

import java.time.LocalDateTime;

import com.minh.simple_typing_game.entity.enums.Difficulty;
import com.minh.simple_typing_game.entity.enums.GameStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game_sessions")
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_sample_id")
    private TextSample textSample;

    // Game metrics
    private Integer wpm; // Words per minute
    private Double accuracy; // Percentage
    private Integer duration; // Seconds taken
    private Integer totalCharacters;
    private Integer correctCharacters;
    private Integer incorrectCharacters;

    // Game state
    @Enumerated(EnumType.STRING)
    private GameStatus status; // COMPLETED, ABANDONED, IN_PROGRESS

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty; // EASY, MEDIUM, HARD

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    // Detailed keystroke data (optional)
    @Column(columnDefinition = "TEXT")
    private String keystrokeData; // JSON string of detailed typing data
}
