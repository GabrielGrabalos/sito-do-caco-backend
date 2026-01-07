package com.caco.sitedocaco.controller.publicController;

import com.caco.sitedocaco.dto.response.ExamWithoutSubjectDTO;
import com.caco.sitedocaco.entity.exam.Exam;
import com.caco.sitedocaco.entity.exam.Subject;
import com.caco.sitedocaco.service.ExamService;
import com.caco.sitedocaco.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/exams")
@RequiredArgsConstructor
public class ExamController {

    private final SubjectService subjectService;
    private final ExamService examService;

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("")
    public ResponseEntity<List<Exam>> getAllExams() {
        List<Exam> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/subject/{subjectCode}")
    public ResponseEntity<List<ExamWithoutSubjectDTO>> getExamsBySubject(@PathVariable String subjectCode) {
        List<ExamWithoutSubjectDTO> exams = examService.getExamsBySubject(subjectCode);
        return ResponseEntity.ok(exams);
    }
}