package com.project.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyEmailRequest {
    @NotBlank(message = "Token cannot be blank")
    private String token;

    @NotBlank(message = "Code cannot be blank")
    @Size(min = 6, max = 6, message = "Code must be 6 characters")
    private String code;
}