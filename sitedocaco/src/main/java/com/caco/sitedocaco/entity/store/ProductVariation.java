package com.caco.sitedocaco.entity.store;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class ProductVariation {
    /*
    2. **`ProductVariation`**
        - `UUID id`
        - `Product product` (ManyToOne)
        - `String name` (ex: "Tamanho P", "Tamanho G")
        - `Integer additionalPrice` (em centavos, ex: 5999 para R$59,99)
        - `Integer stockQuantity`
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private java.util.UUID id;

    @ManyToOne
    private Product product;

    private String name;

    private Integer additionalPrice;

    private Integer stockQuantity;
}
