package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.CreateProfessorDTO;
import com.caco.sitedocaco.dto.request.UpdateProfessorDTO;
import com.caco.sitedocaco.entity.exam.Professor;
import com.caco.sitedocaco.exception.BusinessRuleException;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    @Transactional
    public Professor createProfessor(CreateProfessorDTO dto) {
        if (professorRepository.existsByName(dto.name().trim())) {
            throw new BusinessRuleException("Já existe um professor com este nome");
        }

        Professor professor = new Professor();
        professor.setName(dto.name().trim());

        return professorRepository.save(professor);
    }

    @Transactional(readOnly = true)
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Professor getProfessorById(UUID id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
    }

    @Transactional
    public Professor updateProfessor(UUID id, UpdateProfessorDTO dto) {
        Professor professor = getProfessorById(id);

        if (dto.name() != null && !dto.name().isBlank()) {
            if (professorRepository.existsByName(dto.name().trim())) {
                throw new BusinessRuleException("Já existe um professor com este nome");
            }
            professor.setName(dto.name().trim());
        }

        return professorRepository.save(professor);
    }

    @Transactional
    public void deleteProfessor(UUID id) {
        Professor professor = getProfessorById(id);
        professorRepository.delete(professor);
    }
}

