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


    public TradeFunctionService(CloTradeRepository cloTradeRepository) {
        this.cloTradeRepository = cloTradeRepository;
    }


    public TradeFunctionService() {

    }

    public record Request(String clo, String buy_sell, Long quantity, double price) {
    }

    public record Response(String clo, String buy_sell, Long quantity, double price) {
    }

    @Override
    public Response apply(Request r) {

        CloTrade ctr = new CloTrade(r.clo, r.buy_sell, r.quantity, r.price, false);

        cloTradeRepository.save(ctr);

        return new Response(r.clo, r.buy_sell, r.quantity, r.price);
    }

}