// main/java/com/caco/sitedocaco/repository/ExamRepository.java
package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.entity.enums.ExamType;
import com.caco.sitedocaco.entity.exam.Exam;
import com.caco.sitedocaco.entity.exam.Subject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {
    List<Exam> findBySubjectSubjectCode(String subjectCode);
    List<Exam> findByYear(Integer year);
    List<Exam> findByType(ExamType type);
    List<Exam> findBySubject(Subject subject);
    boolean existsBySubjectSubjectCodeAndYearAndType(String subjectCode, Integer year, ExamType type);
    @Transactional
    @Modifying
    @Query("DELETE FROM Exam e WHERE e.subject.subjectCode = :subjectCode")
    void deleteBySubjectSubjectCode(@Param("subjectCode") String subjectCode);
}