package com.caco.sitedocaco.entity.sticker;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table
public class RedemptionCode {
    /*
    2. **`RedemptionCode`**
    - `String code` (PK, String aleatória de 8-12 chars)
    - `Sticker sticker` (ManyToOne)
    - `Boolean isOneTimeUse`
    - `Boolean isUsed`
    - `LocalDateTime expiresAt` (Nullable)
    */

    @Id
    @Column(length = 12)
    private String code;

    @ManyToOne
    private Sticker sticker;

    private Boolean isOneTimeUse;
    private Boolean isUsed;
    private LocalDateTime expiresAt;
}
