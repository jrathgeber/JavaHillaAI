package com.example.application.services;

import com.example.application.data.CloTradeRepository;
import com.example.application.data.CloTrade;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
@Service
public class TradeService {


    private final OpenAiChatModel chatModel;

    private final CloTradeRepository cloTradeRepository;

    public TradeService(OpenAiChatModel chatModel, CloTradeRepository cloTradeRepository) {

        this.chatModel = chatModel;
        this.cloTradeRepository = cloTradeRepository;

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

        return toCloTradeRecord(saved);
    }


    public String askQuestion(String question) {
        if (question.isEmpty()) {
            return "Please ask a question about CLOs";
        } else {

        ChatResponse cr = chatModel.call(new Prompt(question,
                    OpenAiChatOptions.builder()
                            .withFunction("buyCloFunction")
                            .withFunction("sellCloFunction")
                            .build()));

         return cr.getResult().getOutput().getContent();

        }
    }
}
