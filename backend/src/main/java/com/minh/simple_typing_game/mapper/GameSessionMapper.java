package com.minh.simple_typing_game.mapper;

import org.springframework.stereotype.Component;

import com.minh.simple_typing_game.entity.GameSession;
import com.minh.simple_typing_game.payload.dto.GameSessionDTO;

@Component
public class GameSessionMapper {

    public GameSessionDTO toDTO(GameSession gameSession) {
        if (gameSession == null) {
            return null;
        }
        
        return GameSessionDTO.builder()
                .id(gameSession.getId())
                .userId(gameSession.getUser() != null ? gameSession.getUser().getId() : null)
                .textSampleId(gameSession.getTextSample() != null ? gameSession.getTextSample().getId() : null)
                .textSampleTitle(gameSession.getTextSample() != null ? gameSession.getTextSample().getTitle() : null)
                .textSampleContent(gameSession.getTextSample() != null ? gameSession.getTextSample().getContent() : null)
                .wpm(gameSession.getWpm())
                .accuracy(gameSession.getAccuracy())
                .duration(gameSession.getDuration())
                .totalCharacters(gameSession.getTotalCharacters())
                .correctCharacters(gameSession.getCorrectCharacters())
                .incorrectCharacters(gameSession.getIncorrectCharacters())
                .status(gameSession.getStatus())
                .difficulty(gameSession.getDifficulty())
                .startedAt(gameSession.getStartedAt())
                .completedAt(gameSession.getCompletedAt())
                .keystrokeData(gameSession.getKeystrokeData())
                .build();
    }

    public GameSession toEntity(GameSessionDTO gameSessionDTO) {
        if (gameSessionDTO == null) {
            return null;
        }
        
        GameSession gameSession = new GameSession();
        gameSession.setId(gameSessionDTO.getId());
        gameSession.setWpm(gameSessionDTO.getWpm());
        gameSession.setAccuracy(gameSessionDTO.getAccuracy());
        gameSession.setDuration(gameSessionDTO.getDuration());
        gameSession.setTotalCharacters(gameSessionDTO.getTotalCharacters());
        gameSession.setCorrectCharacters(gameSessionDTO.getCorrectCharacters());
        gameSession.setIncorrectCharacters(gameSessionDTO.getIncorrectCharacters());
        gameSession.setStatus(gameSessionDTO.getStatus());
        gameSession.setDifficulty(gameSessionDTO.getDifficulty());
        gameSession.setStartedAt(gameSessionDTO.getStartedAt());
        gameSession.setCompletedAt(gameSessionDTO.getCompletedAt());
        gameSession.setKeystrokeData(gameSessionDTO.getKeystrokeData());
        
        return gameSession;
    }
}
