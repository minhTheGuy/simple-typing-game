package com.minh.simple_typing_game.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.minh.simple_typing_game.entity.enums.Difficulty;
import com.minh.simple_typing_game.entity.enums.TextCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "text_samples")
public class TextSample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    
    @Enumerated(EnumType.STRING)
    private TextCategory category; // PROGRAMMING, LITERATURE, NEWS, QUOTES
    
    private Integer wordCount;
    private Integer characterCount;
    private Boolean isActive = true;
    
    private LocalDateTime createdAt;
    
    // One-to-many relationship
    @OneToMany(mappedBy = "textSample")
    private List<GameSession> gameSessions = new ArrayList<>();
}