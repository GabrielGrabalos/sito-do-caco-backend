package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.sticker.UserSticker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserStickerRepository extends JpaRepository<UserSticker, UUID> {
    boolean existsByUserIdAndStickerId(UUID userId, UUID stickerId);

    Page<UserSticker> findAllByUserIdOrderByObtainedAtDesc(UUID userId, Pageable pageable);
}

