package com.minh.simple_typing_game.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minh.simple_typing_game.entity.enums.Difficulty;
import com.minh.simple_typing_game.payload.dto.GameSessionDTO;
import com.minh.simple_typing_game.payload.request.EndGameRequest;
import com.minh.simple_typing_game.service.GameSessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/game-session")
@RequiredArgsConstructor
@Slf4j
public class GameSessionController {

    private final GameSessionService gameSessionService;

    @PostMapping("/start")
    public ResponseEntity<GameSessionDTO> startGameSession(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "MEDIUM") Difficulty difficulty) {
        
        log.info("Starting game session for user: {} with difficulty: {}", userId, difficulty);
        
        try {
            GameSessionDTO gameSession = gameSessionService.startGameSession(userId, difficulty);
            return ResponseEntity.ok(gameSession);
        } catch (IllegalArgumentException e) {
            log.error("Error starting game session: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error starting game session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/end/{sessionId}")
    public ResponseEntity<GameSessionDTO> endGameSession(
            @PathVariable Long sessionId,
            @RequestBody EndGameRequest request) {
        
        log.info("Ending game session: {} with results: WPM={}, Accuracy={}%", 
                sessionId, request.getWpm(), request.getAccuracy());
        
        try {
            GameSessionDTO gameSession = gameSessionService.endGameSession(
                sessionId,
                request.getWpm(),
                request.getAccuracy(),
                request.getDuration(),
                request.getTotalCharacters(),
                request.getCorrectCharacters(),
                request.getIncorrectCharacters()
            );
            return ResponseEntity.ok(gameSession);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error ending game session: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error ending game session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/abandon/{sessionId}")
    public ResponseEntity<GameSessionDTO> abandonGameSession(@PathVariable Long sessionId) {
        log.info("Abandoning game session: {}", sessionId);
        
        try {
            GameSessionDTO gameSession = gameSessionService.abandonGameSession(sessionId);
            return ResponseEntity.ok(gameSession);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error abandoning game session: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error abandoning game session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<GameSessionDTO>> getUserGameHistory(@PathVariable Long userId) {
        log.info("Getting game history for user: {}", userId);
        
        try {
            List<GameSessionDTO> history = gameSessionService.getUserGameHistory(userId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            log.error("Error getting game history for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/active/{userId}")
    public ResponseEntity<GameSessionDTO> getActiveGameSession(@PathVariable Long userId) {
        log.info("Getting active game session for user: {}", userId);
        
        try {
            GameSessionDTO activeSession = gameSessionService.getActiveGameSession(userId);
            if (activeSession != null) {
                return ResponseEntity.ok(activeSession);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            log.error("Error getting active game session for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
