package com.minh.simple_typing_game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "This is a test endpoint. If you see this, the server is running correctly.";
    }

}
