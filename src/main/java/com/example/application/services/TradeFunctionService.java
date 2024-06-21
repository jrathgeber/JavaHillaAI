package com.example.application.services;

import java.util.function.Function;

import com.example.application.data.CloRepository;
import com.example.application.data.CloTrade;
import com.example.application.data.CloTradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.services.TradeFunctionService.Request;
import com.example.application.services.TradeFunctionService.Response;



@Service
public class TradeFunctionService implements Function<Request, Response> {

    private final CloTradeRepository cloTradeRepository;

    public TradeFunctionService(CloTradeRepository cloTradeRepository) {
        this.cloTradeRepository = cloTradeRepository;

    }


    // Request for RectangleAreaService
    public record Request(String order) {}
    public record Response(String outcome) {}

    @Override
    public Response apply(Request r) {

        CloTrade ctdummy = new CloTrade("MP30", "Buy", 500000, 99, false);
        cloTradeRepository.save(ctdummy);

        return new Response(r.order);
    }

}