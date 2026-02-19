package com.caco.sitedocaco.dto.response.sticker;

import java.util.List;

public record RedemptionCodeBatchResponseDTO(
        int quantity,
        List<String> codes
) {
}

