package com.example.application.services;

import com.example.application.data.CloReport;
import com.example.application.data.CloTradeRepository;
import com.example.application.data.CloTrade;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@BrowserCallable
@AnonymousAllowed
@Service
public class TradeService {


    private final OpenAiChatModel chatModel;

    private final CloTradeRepository cloTradeRepository;

    private final SimpleVectorStore simpleVectorStore;

    public TradeService(OpenAiChatModel chatModel, CloTradeRepository cloTradeRepository, SimpleVectorStore vectorStore) {

        this.chatModel = chatModel;
        this.cloTradeRepository = cloTradeRepository;
        this.simpleVectorStore = vectorStore;

    }

    public record CloTradeRecord(
            @NotNull
            Long id,
            String clo,
            String dir,
            long quantity,
            double price,
            boolean cancelled
    ) {
    }

    private CloTradeRecord toCloTradeRecord(CloTrade c) {
        return new CloTradeRecord(
                c.getId(),
                c.getClo(),
                c.getDir(),
                c.getQuantity(),
                c.getPrice(),
                c.isCancelled()
        );
    }

    public List<CloTradeRecord> findAllCloTrades() {
        return cloTradeRepository.findAll().stream()
                .map(this::toCloTradeRecord).toList();
    }


    public CloTradeRecord save(CloTradeRecord ctr) {

        var dbClo = cloTradeRepository.findById(ctr.id).orElseThrow();

        dbClo.setClo(ctr.clo);
        dbClo.setDir(ctr.dir);
        dbClo.setQuantity(ctr.quantity);
        dbClo.setPrice(ctr.price);
        dbClo.setCancelled(ctr.cancelled);

        var saved = cloTradeRepository.save(dbClo);

        List<Document> documentList = new ArrayList<Document>();
        documentList.add(convertToDocument(dbClo));
        simpleVectorStore.add(documentList);

        return toCloTradeRecord(saved);
    }

    private static Document convertToDocument(CloTrade ctr) {
        // Convert object to a map of metadata
        Map<String, Object> metadata = Map.of(
                "id", ctr.getId(),
                // Add other fields as needed
                "quantity", ctr.getQuantity()
        );

        // Create a Document
        return new Document(ctr.toString(), metadata);
    }

    public String askQuestion(String question) {
        if (question.isEmpty()) {
            return "Please ask a question about CLOs";
        } else {


        ChatResponse cr = chatModel.call(new Prompt(question,
                    OpenAiChatOptions.builder()
                            .withUser("You are a CLO broker that can buy and sell CLOs. Always ask for a quantity before transacting.")
                            .withFunction("buyCloFunction")
                            .withFunction("sellCloFunction")
                            .build()));

         return cr.getResult().getOutput().getContent();

        }
    }
}
