package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.ProfileDto;
import tn.sahti.backend.dto.ProfileRequest;
import tn.sahti.backend.entity.ClientProfile;
import tn.sahti.backend.repository.ClientProfileRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClientProfileService {

    private final UserRepository userRepository;
    private final ClientProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public ProfileDto get(Authentication authentication) {
        var user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compte introuvable"));
        return ProfileDto.from(user, profileRepository.findByUserId(user.getId()).orElse(null));
    }

    @Transactional
    public ProfileDto save(Authentication authentication, ProfileRequest request) {
        if (request.birthDate().isBefore(LocalDate.now().minusYears(110))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La date de naissance est invalide");
        }
        var user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compte introuvable"));
        var profile = profileRepository.findByUserId(user.getId())
                .orElseGet(() -> ClientProfile.builder().user(user).build());
        profile.setGender(request.gender());
        profile.setBirthDate(request.birthDate());
        profile.setHeightCm(request.heightCm());
        profile.setWeightKg(request.weightKg());
        profile.setGoal(request.goal());
        profile.setActivityLevel(request.activityLevel());
        profile.setDietaryPreferences(normalize(request.dietaryPreferences()));
        profile.setAllergies(normalize(request.allergies()));
        return ProfileDto.from(user, profileRepository.save(profile));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
