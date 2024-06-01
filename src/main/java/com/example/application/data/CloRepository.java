package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CloRepository extends JpaRepository<Clo, Long> {
//    @Query("SELECT c FROM CLO c")
//    List<Clo> findAllClos();
}
