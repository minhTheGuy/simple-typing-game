package com.minh.simple_typing_game.payload.dto;

import java.time.LocalDateTime;

import com.minh.simple_typing_game.entity.enums.Difficulty;
import com.minh.simple_typing_game.entity.enums.GameStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameSessionDTO {

    private Long id;
    private Long userId;
    private Long textSampleId;
    private String textSampleTitle;
    private String textSampleContent;
    
    // Game metrics
    private Integer wpm; // Words per minute
    private Double accuracy; // Percentage
    private Integer duration; // Seconds taken
    private Integer totalCharacters;
    private Integer correctCharacters;
    private Integer incorrectCharacters;
    
    // Game state
    private GameStatus status; // COMPLETED, ABANDONED, IN_PROGRESS
    private Difficulty difficulty; // EASY, MEDIUM, HARD
    
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    
    // Additional data
    private String keystrokeData; // JSON string of detailed typing data
}
