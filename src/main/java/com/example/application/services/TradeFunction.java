package com.example.application.services;

import java.util.function.Function;

import com.example.application.data.CloRepository;
import com.example.application.data.CloTradeRepository;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;



@Configuration
public class TradeFunction {

    private final CloTradeRepository cloTradeRepository;

    private final SimpleVectorStore simpleVectorStore;

    public TradeFunction(CloTradeRepository cloTradeRepository, SimpleVectorStore vectorStore) {
        this.cloTradeRepository = cloTradeRepository;
        this.simpleVectorStore = vectorStore;

    }

    @Bean
    @Description("Buy a CLO given an instruction to do so. ")
    public Function<TradeFunctionService.Request, TradeFunctionService.Response> buyCloFunction() {
        return new TradeFunctionService(cloTradeRepository, simpleVectorStore, "Buy");
    }


    @Bean
    @Description("Sell a CLO given an instruction to do so. ")
    public Function<TradeFunctionService.Request, TradeFunctionService.Response> sellCloFunction() {
        return new TradeFunctionService(cloTradeRepository, simpleVectorStore, "Sell");
    }

}