package com.minh.simple_typing_game.service;

public interface TextSampleService {

    /**
     * Retreives a random text sample from the database.
     * 
     * @return a random text sample as a String
     * @throws IllegalStateException if no active text samples are found
     */
    String getRandomTextSample() throws IllegalStateException;

}