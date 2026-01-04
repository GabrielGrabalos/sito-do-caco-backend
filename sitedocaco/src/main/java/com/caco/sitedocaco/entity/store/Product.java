package com.caco.sitedocaco.entity.store;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table
public class Product {
    /*
    1. **`Product`**
        - `UUID id`
        - `String name` (ex: "Moletom do Curso 2026")
        - `String description` (Markdown)
        - `Integer price` (em centavos, ex: 5999 para R$59,99)
        - `String coverImage`
        - `List<String> galleryImages` (ElementCollection ou tabela separada)
        - `ProductStatus status` (Enum: `AVAILABLE`, `OUT_OF_STOCK`, `PRE_ORDER`)
        - `ProductCategory category` (Enum ou Entidade: `ROUPAS`, `CANECAS`, `ADESIVOS_FISICOS`, `OUTROS`)
    */

    public enum ProductStatus {
        AVAILABLE,
        OUT_OF_STOCK,
        PRE_ORDER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private java.math.BigDecimal price;

    private String coverImage;

    @ElementCollection
    @CollectionTable(name = "product_gallery_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private java.util.List<String> galleryImages;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private String category;
}
