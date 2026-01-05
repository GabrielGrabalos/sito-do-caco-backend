package com.caco.sitedocaco.controller.privateController;

import com.caco.sitedocaco.dto.request.UpdateProfileDTO;
import com.caco.sitedocaco.dto.response.UserResponseDTO;
import com.caco.sitedocaco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/private/me
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        return ResponseEntity.ok(userService.getMe());
    }

    // PUT /api/private/me
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(@RequestBody UpdateProfileDTO dto) {
        return ResponseEntity.ok(userService.updateProfile(dto));
    }
}