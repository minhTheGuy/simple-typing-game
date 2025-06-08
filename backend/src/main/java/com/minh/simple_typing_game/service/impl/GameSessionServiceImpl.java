package com.minh.simple_typing_game.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.minh.simple_typing_game.entity.GameSession;
import com.minh.simple_typing_game.entity.TextSample;
import com.minh.simple_typing_game.entity.User;
import com.minh.simple_typing_game.entity.enums.Difficulty;
import com.minh.simple_typing_game.entity.enums.GameStatus;
import com.minh.simple_typing_game.mapper.GameSessionMapper;
import com.minh.simple_typing_game.payload.dto.GameSessionDTO;
import com.minh.simple_typing_game.repository.GameSessionRepository;
import com.minh.simple_typing_game.repository.TextSampleRepository;
import com.minh.simple_typing_game.repository.UserRepository;
import com.minh.simple_typing_game.service.GameSessionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final TextSampleRepository textSampleRepository;
    private final UserRepository userRepository;
    private final GameSessionMapper gameSessionMapper;

    @Override
    public GameSessionDTO startGameSession(Long userId, Difficulty difficulty) {
        log.info("Starting game session for user: {} with difficulty: {}", userId, difficulty);
        
        // Check if user already has an active session
        var activeSession = gameSessionRepository.findByUserIdAndStatus(userId, GameStatus.IN_PROGRESS);
        if (activeSession.isPresent()) {
            log.warn("User {} already has an active game session", userId);
            return gameSessionMapper.toDTO(activeSession.get());
        }
        
        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        // Get random text sample based on difficulty
        TextSample textSample = getRandomTextSampleByDifficulty(difficulty);
        
        // Create new game session
        GameSession gameSession = new GameSession();
        gameSession.setUser(user);
        gameSession.setTextSample(textSample);
        gameSession.setDifficulty(difficulty);
        gameSession.setStatus(GameStatus.IN_PROGRESS);
        gameSession.setStartedAt(LocalDateTime.now());
        
        // Set text sample metrics
        gameSession.setTotalCharacters(textSample.getCharacterCount());
        
        GameSession savedSession = gameSessionRepository.save(gameSession);
        log.info("Game session started with ID: {} for user: {}", savedSession.getId(), userId);
        
        return gameSessionMapper.toDTO(savedSession);
    }

    @Override
    public GameSessionDTO endGameSession(Long sessionId, Integer wpm, Double accuracy, 
                                       Integer duration, Integer totalCharacters, 
                                       Integer correctCharacters, Integer incorrectCharacters) {
        log.info("Ending game session: {} with WPM: {}, Accuracy: {}%", sessionId, wpm, accuracy);
        
        GameSession gameSession = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with id: " + sessionId));
        
        if (gameSession.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game session is not in progress");
        }
        
        // Update game metrics
        gameSession.setWpm(wpm);
        gameSession.setAccuracy(accuracy);
        gameSession.setDuration(duration);
        gameSession.setTotalCharacters(totalCharacters);
        gameSession.setCorrectCharacters(correctCharacters);
        gameSession.setIncorrectCharacters(incorrectCharacters);
        gameSession.setStatus(GameStatus.COMPLETED);
        gameSession.setCompletedAt(LocalDateTime.now());
        
        GameSession savedSession = gameSessionRepository.save(gameSession);
        log.info("Game session completed: {} for user: {}", sessionId, gameSession.getUser().getId());
        
        return gameSessionMapper.toDTO(savedSession);
    }

    @Override
    public List<GameSessionDTO> getUserGameHistory(Long userId) {
        log.info("Getting game history for user: {}", userId);
        
        List<GameSession> completedSessions = gameSessionRepository
                .findByUserIdAndStatusOrderByStartedAtDesc(userId, GameStatus.COMPLETED);
        
        return completedSessions.stream()
                .map(gameSessionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GameSessionDTO getActiveGameSession(Long userId) {
        log.info("Getting active game session for user: {}", userId);
        
        return gameSessionRepository.findByUserIdAndStatus(userId, GameStatus.IN_PROGRESS)
                .map(gameSessionMapper::toDTO)
                .orElse(null);
    }

    @Override
    public GameSessionDTO abandonGameSession(Long sessionId) {
        log.info("Abandoning game session: {}", sessionId);
        
        GameSession gameSession = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with id: " + sessionId));
        
        if (gameSession.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game session is not in progress");
        }
        
        gameSession.setStatus(GameStatus.ABANDONED);
        gameSession.setCompletedAt(LocalDateTime.now());
        
        GameSession savedSession = gameSessionRepository.save(gameSession);
        log.info("Game session abandoned: {} for user: {}", sessionId, gameSession.getUser().getId());
        
        return gameSessionMapper.toDTO(savedSession);
    }
    
    private TextSample getRandomTextSampleByDifficulty(Difficulty difficulty) {
        // First try to get a sample with the specified difficulty
        List<TextSample> samples = textSampleRepository.findByDifficultyAndIsActiveTrue(difficulty);
        
        if (!samples.isEmpty()) {
            // Return a random sample from the filtered list
            int randomIndex = (int) (Math.random() * samples.size());
            return samples.get(randomIndex);
        }
        
        // If no samples found for the difficulty, fall back to any random active sample
        log.warn("No text samples found for difficulty: {}, falling back to random sample", difficulty);
        return textSampleRepository.findRandomActiveSample()
                .orElseThrow(() -> new IllegalStateException("No active text samples found in the database"));
    }
}