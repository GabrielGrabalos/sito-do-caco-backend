package com.caco.sitedocaco.controller.privateController;

import com.caco.sitedocaco.dto.request.UserProfileFormRequest;
import com.caco.sitedocaco.dto.response.UserProfileResponse;
import com.caco.sitedocaco.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile-form")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * POST /api/user/profile-form
     * Submete o formulário de perfil. Só pode ser feito uma vez.
     */
    @PostMapping
    public ResponseEntity<UserProfileResponse> submitForm(@Valid @RequestBody UserProfileFormRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.submitForm(request));
    }

    /**
     * GET /api/user/profile-form
     * Retorna as respostas do formulário do usuário logado.
     */
    @GetMapping
    public ResponseEntity<UserProfileResponse> getMyProfileForm() {
        return ResponseEntity.ok(userProfileService.getMyProfile());
    }
}

