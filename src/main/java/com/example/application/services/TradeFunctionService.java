package com.example.application.services;

import java.util.function.Function;

import com.example.application.data.CloTrade;
import com.example.application.data.CloTradeRepository;
import org.springframework.stereotype.Service;

import com.example.application.services.TradeFunctionService.Request;
import com.example.application.services.TradeFunctionService.Response;


@Service
public class TradeFunctionService implements Function<Request, Response> {

    private CloTradeRepository cloTradeRepository = null;

    private String dir = "Undef";

    public TradeFunctionService(CloTradeRepository cloTradeRepository, String dir) {
        this.cloTradeRepository = cloTradeRepository;
        this.dir = dir;
    }

    public TradeFunctionService(CloTradeRepository cloTradeRepository) {
        this.cloTradeRepository = cloTradeRepository;
    }

    public TradeFunctionService(){

    }

    public record Request(Long quantity, double price) {}
    public record Response(Long quantity, double price) {}

    @Override
    public Response apply(Request r) {

        CloTrade ctdummy = new CloTrade("MP30", dir, r.quantity, r.price, false);

        cloTradeRepository.save(ctdummy);

        return new Response(r.quantity, r.price);
    }

}