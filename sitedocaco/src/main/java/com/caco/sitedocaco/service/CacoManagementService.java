package com.caco.sitedocaco.service;

import com.sitedocaco.entity.caco.CacoManagement;
import com.sitedocaco.repository.CacoManagementRepository;
import com.sitedocaco.dto.request.CreateCacoManagementDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CacoManagementService{

    private final CacoManagementRepository repository;

    public List<CacoManagement> findAll(){
        return repository.findAllCacoManagements();
    }

    public CacoManagement createManagement(CreateCacoManagementDTO dto){
        repository.save
    }

    public void deleteManagement(){

    }
}