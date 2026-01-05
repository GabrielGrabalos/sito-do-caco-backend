package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.UpdateProfileDTO;
import com.caco.sitedocaco.dto.response.UserResponseDTO;
import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Pega o e-mail do Token JWT (via SecurityContext) e busca o usuário no banco.
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário logado não encontrado no banco."));

        return UserResponseDTO.fromEntity(user);
    }

    /**
     * Atualiza dados básicos do perfil
     */
    @Transactional
    public UserResponseDTO updateProfile(UpdateProfileDTO request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (request.name() != null && !request.name().isBlank()) {
            user.setUsername(request.name());
        }
        if (request.avatarUrl() != null && !request.avatarUrl().isBlank()) {
            user.setAvatarUrl(request.avatarUrl());
        }

        User savedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(savedUser);
    }
}