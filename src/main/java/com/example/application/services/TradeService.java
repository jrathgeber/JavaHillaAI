package com.example.application.services;

import com.example.application.data.CloTradeRepository;
import com.example.application.data.CloTrade;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
@Service
public class TradeService {

    private final CloService cloService;
    private final CloTradeRepository cloTradeRepository;

    public TradeService(CloService cloService, CloTradeRepository cloTradeRepository) {

        this.cloService = cloService;
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


            //CloTrade cr = cloService.getCloTrade();
            //cloTradeRepository.save(cr);

            //List<CloTrade> cll = cloService.getDataElements();

            //for(CloTrade cl : cll)
            //{
            //    cloTradeRepository.save(cl);
            //}

            return cloService.askQuestion(question);
        }
    }
}
