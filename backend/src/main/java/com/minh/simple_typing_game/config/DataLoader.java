package com.minh.simple_typing_game.config;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.minh.simple_typing_game.entity.TextSample;
import com.minh.simple_typing_game.entity.enums.Difficulty;
import com.minh.simple_typing_game.entity.enums.TextCategory;
import com.minh.simple_typing_game.repository.TextSampleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final TextSampleRepository textSampleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadSampleTexts();
    }

    private void loadSampleTexts() {
        if (textSampleRepository.count() > 0) {
            log.info("Text samples already exist, skipping data loading");
            return;
        }

        log.info("Loading sample text data...");

        List<TextSample> samples = Arrays.asList(
            createTextSample(
                "Quick Brown Fox",
                "The quick brown fox jumps over the lazy dog and runs through the forest with amazing speed and agility.",
                Difficulty.EASY,
                TextCategory.LITERATURE
            ),
            createTextSample(
                "Programming Basics",
                "Programming is the art of telling another human being what one wants the computer to do with precision and clarity. It requires logical thinking and attention to detail.",
                Difficulty.MEDIUM,
                TextCategory.PROGRAMMING
            ),
            createTextSample(
                "Vue.js Framework",
                "Vue.js is a progressive framework for building user interfaces that focuses on the view layer and can be easily integrated into projects using other libraries.",
                Difficulty.MEDIUM,
                TextCategory.PROGRAMMING
            ),
            createTextSample(
                "Advanced Algorithm",
                "Dynamic programming is an algorithmic paradigm that solves complex problems by breaking them down into simpler subproblems. It is applicable to problems exhibiting overlapping subproblems and optimal substructure properties.",
                Difficulty.HARD,
                TextCategory.PROGRAMMING
            ),
            createTextSample(
                "Technology News",
                "Artificial intelligence and machine learning technologies are rapidly transforming industries across the globe, creating new opportunities while presenting unique challenges for businesses and society.",
                Difficulty.MEDIUM,
                TextCategory.NEWS
            ),
            createTextSample(
                "Motivational Quote",
                "Success is not final, failure is not fatal: it is the courage to continue that counts. The only way to do great work is to love what you do.",
                Difficulty.EASY,
                TextCategory.QUOTES
            ),
            createTextSample(
                "Complex Literature",
                "In the midst of winter, I found there was, within me, an invincible summer. And that makes me happy. For it says that no matter how hard the world pushes against me, within me, there's something stronger.",
                Difficulty.HARD,
                TextCategory.LITERATURE
            ),
            createTextSample(
                "Database Concepts",
                "Relational databases use structured query language (SQL) to manage and manipulate data stored in tables with rows and columns, ensuring data integrity through ACID properties.",
                Difficulty.HARD,
                TextCategory.PROGRAMMING
            )
        );

        textSampleRepository.saveAll(samples);
        log.info("Loaded {} text samples successfully", samples.size());
    }

    private TextSample createTextSample(String title, String content, Difficulty difficulty, TextCategory category) {
        TextSample sample = new TextSample();
        sample.setTitle(title);
        sample.setContent(content);
        sample.setDifficulty(difficulty);
        sample.setCategory(category);
        sample.setWordCount(content.split("\\s+").length);
        sample.setCharacterCount(content.length());
        sample.setIsActive(true);
        sample.setCreatedAt(LocalDateTime.now());
        return sample;
    }
}
