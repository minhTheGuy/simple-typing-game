package com.minh.simple_typing_game.service.impl;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.minh.simple_typing_game.entity.TextSample;
import com.minh.simple_typing_game.repository.TextSampleRepository;
import com.minh.simple_typing_game.service.TextSampleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TextSampleServiceImpl implements TextSampleService {

    private final TextSampleRepository textSampleRepository;
    private final Random random = new Random();

    @Override
    public String getRandomTextSample() throws IllegalStateException {
        List<TextSample> activeSamples = textSampleRepository.findByIsActiveTrue();
        
        if (activeSamples.isEmpty()) {
            throw new IllegalStateException("No active text samples found");
        }
        
        int randomIndex = random.nextInt(activeSamples.size());
        return activeSamples.get(randomIndex).getContent();
    }
}
