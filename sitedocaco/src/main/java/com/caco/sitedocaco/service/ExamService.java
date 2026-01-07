package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.CreateExamDTO;
import com.caco.sitedocaco.dto.request.UpdateExamDTO;
import com.caco.sitedocaco.dto.response.ExamWithoutSubjectDTO;
import com.caco.sitedocaco.entity.exam.Exam;
import com.caco.sitedocaco.entity.exam.Subject;
import com.caco.sitedocaco.exception.BusinessRuleException;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final SubjectService subjectService;

    @Transactional
    public Exam createExam(CreateExamDTO dto) {
        Subject subject = subjectService.getSubjectByCode(dto.subjectCode());

        boolean exists = examRepository.existsBySubjectSubjectCodeAndYearAndType(
                dto.subjectCode(), dto.year(), dto.type()
        );

        if (exists) {
            throw new BusinessRuleException(
                    String.format("Já existe uma prova do tipo %s para %s no ano %d",
                            dto.type(), subject.getName(), dto.year())
            );
        }

        Exam exam = new Exam();
        exam.setSubject(subject);
        exam.setYear(dto.year());
        exam.setType(dto.type());
        exam.setFileUrl(dto.fileUrl());

        return examRepository.save(exam);
    }

    @Transactional(readOnly = true)
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Exam getExamById(UUID id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prova não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<ExamWithoutSubjectDTO> getExamsBySubject(String subjectCode) {
        Subject subject = subjectService.getSubjectByCode(subjectCode);
        return examRepository.findBySubject(subject)
                .stream()
                .map(exam -> new ExamWithoutSubjectDTO(
                        exam.getId(),
                        exam.getYear(),
                        exam.getType(),
                        exam.getFileUrl()
                ))
                .toList();
    }

    @Transactional
    public Exam updateExam(UUID id, UpdateExamDTO dto) {
        Exam exam = getExamById(id);

        if (dto.subjectCode() != null) {
            Subject newSubject = subjectService.getSubjectByCode(dto.subjectCode());
            exam.setSubject(newSubject);
        }

        if (dto.year() != null) {
            exam.setYear(dto.year());
        }

        if (dto.type() != null) {
            exam.setType(dto.type());
        }

        if (dto.fileUrl() != null) {
            exam.setFileUrl(dto.fileUrl());
        }

        return examRepository.save(exam);
    }

    @Transactional
    public void deleteExam(UUID id) {
        Exam exam = getExamById(id);
        examRepository.delete(exam);
    }

    public List<ExamWithoutSubjectDTO> getExamsBySubjectCode(String subjectCode) {
        List<Exam> exams = examRepository.findBySubjectSubjectCode(subjectCode);
        return exams.stream()
                .map(exam -> new ExamWithoutSubjectDTO(
                        exam.getId(),
                        exam.getYear(),
                        exam.getType(),
                        exam.getFileUrl()
                ))
                .toList();
    }
}