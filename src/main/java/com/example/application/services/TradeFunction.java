package com.example.application.services;

import java.util.function.Function;

import com.example.application.data.CloRepository;
import com.example.application.data.CloTradeRepository;
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
    @Description("Buy a CLO given an instruction to do so. ")
    public Function<TradeFunctionService.Request, TradeFunctionService.Response> buyCloFunction() {
        return new TradeFunctionService(cloTradeRepository, "Buy");
    }


    @Bean
    @Description("Sell a CLO given an instruction to do so. ")
    public Function<TradeFunctionService.Request, TradeFunctionService.Response> sellCloFunction() {
        return new TradeFunctionService(cloTradeRepository, "Sell");
    }

}