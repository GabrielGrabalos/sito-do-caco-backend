package com.caco.sitedocaco.entity.exam;

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

    public enum ExamType {
        P1,
        P2,
        P3,
        EXAME,
        SUB,
        OUTROS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Subject subject;

    private Integer year;
    @Enumerated(EnumType.STRING)
    private ExamType type;

    @Column(nullable = false)
    private String fileUrl;
}
