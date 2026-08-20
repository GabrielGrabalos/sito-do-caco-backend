package com.caco.sitedocaco.controller.entity.caco;


import caco.sitedocaco.entity.Caco.CacoManagementMember;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "caco_management")
@Data
public class CacoManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String Start_Date;
    
    @column(nullable = false)
    private String End_Date;

    @OneToMany(mappedBy = "cacoManagement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CacoManagementMember> members;
 
}