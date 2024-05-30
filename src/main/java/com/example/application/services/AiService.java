package com.example.application.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

import org.springframework.ai.chat.ChatClient;

@BrowserCallable
@AnonymousAllowed
@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    public String askQuestion(String question) {
        if (question.isEmpty()) {
            return "Hello stranger";
        } else {
            return chatClient.call(question);
        }
    }
}
