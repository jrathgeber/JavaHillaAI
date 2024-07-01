package com.example.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.example.application.data.CloTrade;
import com.example.application.data.CloTradeRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;

import com.example.application.services.TradeFunctionService.Request;
import com.example.application.services.TradeFunctionService.Response;


@Service
public class TradeFunctionService implements Function<Request, Response> {

    private CloTradeRepository cloTradeRepository = null;

    private SimpleVectorStore simpleVectorStore = null;

    private String dir = "Undef";

    public TradeFunctionService(CloTradeRepository cloTradeRepository, SimpleVectorStore vectorStore, String dir) {
        this.cloTradeRepository = cloTradeRepository;
        this.simpleVectorStore = vectorStore;
        this.dir = dir;
    }

    public TradeFunctionService(CloTradeRepository cloTradeRepository, SimpleVectorStore vectorStore) {
        this.cloTradeRepository = cloTradeRepository;
        this.simpleVectorStore = vectorStore;
    }

    public TradeFunctionService() {

    }

    public record Request(Long quantity, double price) {
    }

    public record Response(Long quantity, double price) {
    }

    @Override
    public Response apply(Request r) {

        CloTrade ctdummy = new CloTrade("MP30", dir, r.quantity, r.price, false);

        cloTradeRepository.save(ctdummy);


        List<Document> documentList = new ArrayList<Document>();
        documentList.add(convertToDocument(ctdummy));
        simpleVectorStore.add(documentList);

        return new Response(r.quantity, r.price);
    }

    private static Document convertToDocument(CloTrade ctr) {
        // Convert object to a map of metadata
        Map<String, Object> metadata = Map.of(
                "id", ctr.getId(),
                // Add other fields as needed
                "quantity", ctr.getQuantity(),
                "clo", ctr.getClo()
        );
        return new Document(ctr.toString(), metadata);
    }
}