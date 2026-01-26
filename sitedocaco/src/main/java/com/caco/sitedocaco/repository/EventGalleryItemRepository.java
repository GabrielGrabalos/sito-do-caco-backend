package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.event.EventGalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventGalleryItemRepository extends JpaRepository<EventGalleryItem, UUID> {
    List<EventGalleryItem> findByEventIdOrderByIdAsc(UUID eventId);
    void deleteByEventId(UUID eventId);
}