package tn.sahti.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.sahti.backend.dto.ProfileDto;
import tn.sahti.backend.dto.ProfileRequest;
import tn.sahti.backend.service.ClientProfileService;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ClientProfileService profileService;

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ProfileDto me(Authentication authentication) {
        return profileService.get(authentication);
    }

    @PutMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ProfileDto save(Authentication authentication, @Valid @RequestBody ProfileRequest request) {
        return profileService.save(authentication, request);
    }
}