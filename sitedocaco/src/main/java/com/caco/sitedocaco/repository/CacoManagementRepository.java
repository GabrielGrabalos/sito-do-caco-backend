package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.management.CacoManagement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.readOnly.Param;

import java.util.UUID;


@Repository
public interface CacoManagementRepository extends JpaRepository<CacoManagement, UUID> {

    @Query("SELECT caco FROM CacoManagement caco ORDER BY caco.startDate DESC")
    Page<CacoManagement> findAllCacoManagements(Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM CacoManagement caco WHERE caco.id = :cacoManagementId")
    void deleteById(@Param("cacoManagementId") UUID cacoManagementId);
}