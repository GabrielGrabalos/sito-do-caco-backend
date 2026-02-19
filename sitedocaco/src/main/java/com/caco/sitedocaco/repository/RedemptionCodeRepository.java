package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.sticker.RedemptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedemptionCodeRepository extends JpaRepository<RedemptionCode, String> {
    boolean existsByCode(String code);
}

