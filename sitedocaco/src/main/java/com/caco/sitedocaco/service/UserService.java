package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.UpdateProfileDTO;
import com.caco.sitedocaco.dto.response.UserResponseDTO;
import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.entity.enums.ImageType;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImgBBService imgBBService;

    /**
     * Pega o e-mail do Token JWT (via SecurityContext) e busca o usuário no banco.
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário logado não encontrado no banco."));

        return UserResponseDTO.fromEntity(user);
    }

    /**
     * Atualiza dados básicos do perfil
     */
    /**
     * Atualiza dados do perfil do usuário logado
     * @param request DTO com campos opcionais (nome e/ou avatar)
     * @return DTO do usuário atualizado
     */
    @Transactional
    public UserResponseDTO updateProfile(UpdateProfileDTO request) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));


        // Atualizar nome se fornecido
        if (request.name() != null && !request.name().isBlank()) {
            user.setUsername(request.name());
        }

        // Processar avatar se fornecido
        if (request.avatar() != null && !request.avatar().isEmpty()) {
            String newAvatarUrl = uploadAvatarImage(request.avatar());
            user.setAvatarUrl(newAvatarUrl);
        }

        // Salvar alterações
        User savedUser = userRepository.save(user);

        return UserResponseDTO.fromEntity(savedUser);
    }

    /**
     * Faz upload da imagem do avatar para o ImgBB
     */
    private String uploadAvatarImage(MultipartFile avatarFile) throws IOException {
        try {
            // Usar o serviço ImgBB para fazer upload
            String imageUrl = imgBBService.uploadImage(avatarFile, ImageType.PROFILE_AVATAR);

            return imageUrl;

        } catch (IOException e) {
            throw new IOException("Não foi possível fazer upload da imagem do perfil", e);
        }
    }

    /**
     * Método auxiliar para obter o usuário atual
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    /**
     * Método para deletar avatar (opcional)
     */
    @Transactional
    public void removeAvatar() {
        User user = getCurrentUser();

        // Aqui você poderia deletar a imagem do ImgBB se necessário
        // Por enquanto, apenas setamos como null
        user.setAvatarUrl(null);
        userRepository.save(user);
    }
}