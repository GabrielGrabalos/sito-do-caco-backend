package com.caco.sitedocaco.dto.request.store;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductVariationDTO(
        String name,

        @PositiveOrZero(message = "O preço adicional deve ser zero ou positivo")
        BigDecimal additionalPrice,

        @Min(value = 0, message = "A quantidade em estoque não pode ser negativa")
        Integer stockQuantity
) {}