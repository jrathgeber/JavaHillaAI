package com.example.application.services;

import com.example.application.data.CloRepository;
import com.example.application.data.Clo;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;
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
    private final CloRepository cloRepository;

    public CloService(ChatClient chatClient, VectorStore vectorStore, CloRepository cloRepository) {

        this.chatClient = chatClient;
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

    public List<CloService.CloRecord> findAllClo() {
        return cloRepository.findAll().stream()
                .map(this::toCloRecord).toList();
    }

    /*

    public List<CloService.CloRecord> findAllClos() {
        List<Clo> all = cloRepository.findAllWithClos();
        return all.stream()
                .map(this::toCloRecord).toList();
    }
*/


    public CloService.CloRecord save(CloService.CloRecord clo) {

        var dbClo = cloRepository.findById(clo.id).orElseThrow();

        dbClo.setName(clo.name);
        dbClo.setLocation(clo.location);

        var saved = cloRepository.save(dbClo);

        return toCloRecord(saved);
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
