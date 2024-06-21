package com.example.application.services;

import com.example.application.data.CloReport;
import com.example.application.data.CloRepository;
import com.example.application.data.Clo;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.converter.MapOutputConverter;

@BrowserCallable
@AnonymousAllowed
@Service
public class CloService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource ragPromptTemplate;
    private final CloRepository cloRepository;



    public CloService(ChatClient.Builder builder, VectorStore vectorStore, CloRepository cloRepository) {

        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore,SearchRequest.defaults()))
                .build();

        this.vectorStore = vectorStore;
        this.cloRepository = cloRepository;

    }

    public record CloRecord(
            @NotNull
            Long id,
            String name,
            String location
    ) {
    }

    private CloService.CloRecord toCloRecord(Clo c) {
        return new CloService.CloRecord(
                c.getId(),
                c.getName(),
                c.getLocation()
        );
    }

    public List<CloService.CloRecord> findAllClos() {
        return cloRepository.findAll().stream()
                .map(this::toCloRecord).toList();
    }


    public CloService.CloRecord save(CloService.CloRecord clo) {

        var dbClo = cloRepository.findById(clo.id).orElseThrow();
        dbClo.setName(clo.name);
        dbClo.setLocation(clo.location);
        var saved = cloRepository.save(dbClo);
        return toCloRecord(saved);

    }


    public List<CloReport> getDataElements() {

        return chatClient.prompt()
                .user("Generate a list of attributes for Madison Park Funding LX Ltd")
                .call()
                .entity(new ParameterizedTypeReference<List<CloReport>>() {
                });
    }


    public CloReport getCloReport() {

        return chatClient.prompt()
                .user(u -> u.text("Find the value {data} of a CLO for {clo}. Return all fields with number 999 if you don't know")
                        .param("clo", "Madison Park Funding LX Ltd").param("name", "Value").param("data", "100"))
                .call()
                .entity(CloReport.class);
    }


    public String askQuestion(String question) {

        if (question.isEmpty()) {
            return "Please ask a question about CLOs";
        } else {

            return chatClient
                    .prompt()
                    .user(question)
                    .call()
                    .content(); // short for getResult().getOutput().getContent();

        }
    }
}
