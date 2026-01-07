package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.exam.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    Optional<Subject> findBySubjectCode(String subjectCode);
    boolean existsBySubjectCode(String subjectCode);
    boolean existsByName(String name);
}