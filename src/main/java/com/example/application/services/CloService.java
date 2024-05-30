package com.example.application.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BrowserCallable
@AnonymousAllowed
@Service
public class CloService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;
    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    public CloService(ChatClient chatClient, VectorStore vectorStore) {

        this.chatClient = chatClient;
        this.vectorStore = vectorStore;

    }


    public String askQuestion(String question) {
        if (question.isEmpty()) {
            return "Please ask a question about CLOs";
        } else {
            List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(2));
            List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
            PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
            Map<String, Object> promptParameters = new HashMap<>();
            promptParameters.put("input", question);
            promptParameters.put("documents", String.join("\n", contentList));
            Prompt prompt = promptTemplate.create(promptParameters);

            return chatClient.call(prompt).getResult().getOutput().getContent();
        }
    }
}
