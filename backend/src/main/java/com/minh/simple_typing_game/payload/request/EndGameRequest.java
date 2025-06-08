package com.minh.simple_typing_game.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndGameRequest {
    
    private Integer wpm; // Words per minute
    private Double accuracy; // Percentage (0-100)
    private Integer duration; // Time taken in seconds
    private Integer totalCharacters;
    private Integer correctCharacters;
    private Integer incorrectCharacters;
    private String keystrokeData; // Optional JSON string of detailed typing data
}
