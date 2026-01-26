package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    // Eventos futuros e em andamento (ordenados por mais próximo)
    @Query("SELECT e FROM Event e WHERE e.endDate >= :now ORDER BY e.startDate ASC")
    Page<Event> findUpcomingEvents(@Param("now") LocalDateTime now, Pageable pageable);

    // Eventos passados (ordenados por mais recente)
    @Query("SELECT e FROM Event e WHERE e.endDate < :now ORDER BY e.startDate DESC")
    Page<Event> findPastEvents(@Param("now") LocalDateTime now, Pageable pageable);
}