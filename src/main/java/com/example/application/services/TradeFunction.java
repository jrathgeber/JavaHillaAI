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

    public TradeFunction(CloTradeRepository cloTradeRepository) {
        this.cloTradeRepository = cloTradeRepository;
    }

    @Bean
    @Description("Buy or Sell a CLO given an instruction to do so. ")
    public Function<TradeFunctionService.Request, TradeFunctionService.Response> buyOrSellCloFunction() {
        return new TradeFunctionService(cloTradeRepository);
    }


}