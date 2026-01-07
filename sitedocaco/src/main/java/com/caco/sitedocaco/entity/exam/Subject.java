package com.caco.sitedocaco.entity.exam;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table
@Data
public class Subject {
    /*
    2. `Subject`
        - `String subjectCode` (Código da disciplina, ex: "MC102", "F159", etc) [PK]
        - `String name`
    */

    @Id
    @Column(nullable = false, unique = true)
    private String subjectCode;

    @Column(nullable = false, unique = true)
    private String name;
}
