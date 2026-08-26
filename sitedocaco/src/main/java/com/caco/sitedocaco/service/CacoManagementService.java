package com.caco.sitedocaco.service;

import com.caco.sitedocaco.entity.caco.CacoManagement;
import com.caco.sitedocaco.repository.CacoManagementRepository;
import com.caco.sitedocaco.dto.request.caco.CreateCacoManagementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CacoManagementService {

    private final CacoManagementRepository repository;

    @Transactional(readOnly = true)
    public Page<CacoManagement> findAll(Pageable pageable) {
        return repository.findAllByOrderByStartDateDesc(pageable);
    }


    @Transactional
    public CacoManagement createManagement(CreateCacoManagementDTO dto) {
        CacoManagement cacoManagement = new CacoManagement();

        cacoManagement.setName(dto.name());
        cacoManagement.setStartDate(dto.startDate());
        cacoManagement.setEndDate(dto.endDate());

        return repository.save(cacoManagement);
    }

    @Transactional
    public void deleteManagement(UUID id) {
        repository.deleteById(id);
    }
}