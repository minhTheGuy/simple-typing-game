package com.minh.simple_typing_game.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.minh.simple_typing_game.entity.TextSample;
import com.minh.simple_typing_game.entity.enums.Difficulty;

@Repository
public interface TextSampleRepository extends JpaRepository<TextSample, Long> {

    List<TextSample> findByDifficultyAndIsActiveTrue(Difficulty difficulty);

    List<TextSample> findByIsActiveTrue();

    List<TextSample> findByDifficulty(Difficulty difficulty);

    /**
     * Finds a random active text sample.
     */
    @Query("SELECT ts FROM TextSample ts WHERE ts.isActive = true ORDER BY RANDOM()")
    Optional<TextSample> findRandomActiveSample();
}
