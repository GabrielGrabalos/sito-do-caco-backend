package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.caco.CacoManagement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CacoManagementRepository extends JpaRepository<CacoManagement, UUID> {

    Page<CacoManagement> findAllByOrderByStartDateDesc(Pageable pageable);
}