package com.caco.sitedocaco.controller.privateController;

import com.caco.sitedocaco.dto.request.UpdateProfileDTO;
import com.caco.sitedocaco.dto.response.UserResponseDTO;
import com.caco.sitedocaco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        return ResponseEntity.ok(userService.getMe());
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws IOException {

        UpdateProfileDTO dto = new UpdateProfileDTO(name, avatar);
        return ResponseEntity.ok(userService.updateProfile(dto));
    }
}