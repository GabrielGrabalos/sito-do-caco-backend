package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.exam.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    boolean existsByName(String name);
}

