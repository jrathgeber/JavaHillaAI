package com.example.application;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final ChatClient chatClient;

    public TestController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/dad-jokes")
    public String joke(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about dogs") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }

    @GetMapping("/jokes-by-topic")
    public String jokesByTopic(@RequestParam String topic) {
        return chatClient.prompt()
                .user(u -> u.text("Tell me a joke about {topic}").param("topic",topic))
                .call()
                .content();
    }

    @GetMapping("jokes-with-response")
    public ChatResponse jokeWithResponse(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about computers") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

    @GetMapping("/faq")
    public String faq(@RequestParam(value = "message", defaultValue = "What is the outlook for the CLO Market in 2024") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}