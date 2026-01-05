package com.caco.sitedocaco.dto.response;

import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.entity.enums.Role;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String avatarUrl,
        Role role
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getRole()
        );
    }
}