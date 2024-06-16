package com.example.application.services;

import com.example.application.data.CloReportRepository;
import com.example.application.data.CloReport;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
@Service
public class ReportService {

    private final CloService cloService;
    private final CloReportRepository cloReportRepository;

    public ReportService(CloService cloService, CloReportRepository cloReportRepository) {

            this.cloService = cloService;
            this.cloReportRepository = cloReportRepository;

    }

    public record CloReportRecord(
            @NotNull
            Long id,
            String clo,
            String name,
            String data
    ) {
    }

    private CloReportRecord toCloReportRecord(CloReport c) {
        return new CloReportRecord(
                c.getId(),
                c.getClo(),
                c.getName(),
                c.getData()
        );
    }

    public List<CloReportRecord> findAllCloReports() {
        return cloReportRepository.findAll().stream()
                .map(this::toCloReportRecord).toList();
    }


    public CloReportRecord save(CloReportRecord clorr) {

        var dbClo = cloReportRepository.findById(clorr.id).orElseThrow();

        dbClo.setClo(clorr.clo);
        dbClo.setName(clorr.name);
        dbClo.setData(clorr.data);

        var saved = cloReportRepository.save(dbClo);

        return toCloReportRecord(saved);
    }

    public String askQuestion(String question) {
        if (question.isEmpty()) {
            return "Please ask a question about CLOs";
        } else {

            CloReport cr = new CloReport("MP", "Size", "Large");
            cloReportRepository.save(cr);

            return cloService.askQuestion(question);
        }
    }
}
