package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CloReportRepository extends JpaRepository<CloReport, Long> {
//    @Query("SELECT c FROM CLO c")
//    List<Clo> findAllClos();
}
