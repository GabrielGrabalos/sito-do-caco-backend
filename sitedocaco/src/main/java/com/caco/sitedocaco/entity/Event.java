package com.caco.sitedocaco.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table
@Data
public class Event {
    /*
    1. **`Event`**
        - `UUID id`
        - `String title`
        - `String description` (Markdown)
        - `LocalDateTime startDate`
        - `LocalDateTime endDate`
        - `String location`
        - `String coverImage`
        - `EventType type` (Enum: `CACO`, `IC`, `FERIADO`)
        - `EventImportance importance` (Enum: `MAJOR`, `MINOR`)
            - *Regra:* `MAJOR` = Tem página própria. `MINOR` = Só modal.
        - `EventStatus status` (Enum: `SCHEDULED`, `HAPPENING`, `ENDED`)
    */

    public enum EventType {
        CACO,
        IC,
        FERIADO
    }

    public enum EventImportance {
        MAJOR,
        MINOR
    }

    public enum EventStatus {
        SCHEDULED,
        HAPPENING,
        ENDED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    @Lob
    private String description;
    private java.time.LocalDateTime startDate;
    private java.time.LocalDateTime endDate;
    private String location;
    private String coverImage;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @Enumerated(EnumType.STRING)
    private EventImportance importance;

    @Enumerated(EnumType.STRING)
    private EventStatus status;
}
