package com.caco.sitedocaco.entity.exam;

import com.caco.sitedocaco.entity.enums.ExamType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table
@Data
public class Exam {
    /*
    1. **`Exam`**
        - `UUID id`
        - `Subject subject` (Disciplina, ManyToOne)
        - `Integer year`
        - `ExamType type` (Enum: `P1`, `P2`, `P3`, `EXAME`, `SUB`, `OUTROS`)
        - `String fileUrl`
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subject_code", nullable = false)
    private Subject subject;

    private Integer year;
    @Enumerated(EnumType.STRING)
    private ExamType type;

    @Column(nullable = false)
    private String fileUrl;
}
