// ResetPasswordRequest.java
package com.project.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.UUID;

@Data
public class ResetPasswordRequest {
    private UUID token;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, max = 40, message = "Password must be 8-40 characters")
    private String newPassword;
}