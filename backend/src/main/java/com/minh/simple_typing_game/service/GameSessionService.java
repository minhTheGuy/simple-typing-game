package com.minh.simple_typing_game.service;

import java.util.List;

import com.minh.simple_typing_game.entity.GameSession;
import com.minh.simple_typing_game.entity.enums.Difficulty;
import com.minh.simple_typing_game.payload.dto.GameSessionDTO;

public interface GameSessionService {

    /**
     * Starts a new game session for the user.
     *
     * @param userId the ID of the user starting the game session
     * @param difficulty the difficulty level for the game
     * @return GameSessionDTO containing session details and text sample
     */
    GameSessionDTO startGameSession(Long userId, Difficulty difficulty);

    /**
     * Ends the current game session for the user with results.
     *
     * @param sessionId the ID of the game session to end
     * @param wpm words per minute achieved
     * @param accuracy accuracy percentage
     * @param duration time taken in seconds
     * @param totalCharacters total characters in the text
     * @param correctCharacters correctly typed characters
     * @param incorrectCharacters incorrectly typed characters
     * @return GameSessionDTO with updated session data
     */
    GameSessionDTO endGameSession(Long sessionId, Integer wpm, Double accuracy, 
                                 Integer duration, Integer totalCharacters, 
                                 Integer correctCharacters, Integer incorrectCharacters);

    /**
     * Gets game session history for a user.
     *
     * @param userId the ID of the user
     * @return list of game sessions ordered by completion date
     */
    List<GameSessionDTO> getUserGameHistory(Long userId);

    /**
     * Gets the current active game session for a user.
     *
     * @param userId the ID of the user
     * @return active GameSessionDTO or null if no active session
     */
    GameSessionDTO getActiveGameSession(Long userId);

    /**
     * Abandons the current game session.
     *
     * @param sessionId the ID of the session to abandon
     * @return updated GameSessionDTO
     */
    GameSessionDTO abandonGameSession(Long sessionId);
}
