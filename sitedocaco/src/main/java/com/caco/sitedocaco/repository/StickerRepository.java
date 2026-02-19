package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.sticker.Sticker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StickerRepository extends JpaRepository<Sticker, UUID> {
    boolean existsByNameIgnoreCase(String name);

    Page<Sticker> findAllByOrderByCreatedAtDesc(Pageable pageable);
}

