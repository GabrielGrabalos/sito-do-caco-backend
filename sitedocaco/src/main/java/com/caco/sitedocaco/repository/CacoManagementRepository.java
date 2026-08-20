package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.management.CacoManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface CacoManagementRepository extends JpaRepository<CacoManagement, UUID> {

    @Query("SELECT caco FROM CacoManagement caco ORDER BY caco.startDate DESC")
    Page<CacoManagement> findAllCacoManagements(Pageable pageable);
}