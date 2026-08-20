package com.caco.sitedocaco.entity.caco;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class CacoManagementMemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String roleName;
}