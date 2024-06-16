package com.example.application.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;

@BrowserCallable
@AnonymousAllowed
@Service
public class FunctionService {

    private final ChatClient chatClient;


    public FunctionService(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    public String askQuestion(String question) {
        if (question.isEmpty()) {

            return "Hello stranger";
        } else {

            return chatClient.prompt()
                    .user(question)
                    .call()
                    .content(); // short for getResult().getOutput().getContent();
        }
    }
}
