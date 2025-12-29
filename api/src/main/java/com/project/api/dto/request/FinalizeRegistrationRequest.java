package com.project.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FinalizeRegistrationRequest {
    @NotBlank(message = "Token cannot be blank")
    private String token;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
    private String username;
}
