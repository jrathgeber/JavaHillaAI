package com.example.application.services;

import com.example.application.data.CloReport;
import com.example.application.data.CloRepository;
import com.example.application.data.Clo;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;

import org.springframework.ai.chat.client.ChatClient;
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


    public String getReportValues(String question) {

        /*

        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(2));
        List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", question);
        promptParameters.put("documents", String.join("\n", contentList));
        Prompt prompt = promptTemplate.create(promptParameters);

         */

        /*

        MapOutputConverter mapOutputConverter = new MapOutputConverter();
        String format = mapOutputConverter.getFormat();
        String userInputTemplate = """
           Provide me a List of {subject}
           {format}
           """;
        PromptTemplate promptTemplate = new PromptTemplate(userInputTemplate,
                Map.of("subject", "an array of numbers from 1 to 9 under they key name 'numbers'", "format", format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());


        Generation generation = chatClient.call(prompt).getResult();
        Map<String, Object> result = mapOutputConverter.convert(generation.getOutput().getContent());



         */
        return "works";


    }


    public List<CloReport> getDataElements() {

        List<CloReport> actorsFilms = chatClient.prompt()
                .user("Generate a list of attributes for Madison Park Funding LX Clo.  ")
                .call()
                .entity(new ParameterizedTypeReference<List<CloReport>>() {
                });

        return actorsFilms;
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
            List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(2));
            List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
            PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
            Map<String, Object> promptParameters = new HashMap<>();
            promptParameters.put("input", question);
            promptParameters.put("documents", String.join("\n", contentList));
            Prompt prompt = promptTemplate.create(promptParameters);


            return chatClient
                    .prompt(prompt)
                    .call()
                    .content(); // short for getResult().getOutput().getContent();

        }
    }
}
