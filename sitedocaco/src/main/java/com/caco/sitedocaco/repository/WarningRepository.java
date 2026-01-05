package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.home.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WarningRepository extends JpaRepository<Warning, UUID> {

    // Regra: O aviso deve ter começado (startsAt <= agora) E não ter expirado (expiresAt > agora)
    @Query("SELECT w FROM Warning w WHERE w.startsAt <= :now AND w.expiresAt > :now ORDER BY w.startsAt DESC")
    List<Warning> findActiveWarnings(LocalDateTime now);
}