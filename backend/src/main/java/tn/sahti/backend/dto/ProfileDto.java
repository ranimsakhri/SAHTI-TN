package tn.sahti.backend.dto;

import tn.sahti.backend.entity.ClientProfile;
import tn.sahti.backend.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProfileDto(
        String email,
        String firstName,
        String lastName,
        List<String> roles,
        String gender,
        LocalDate birthDate,
        BigDecimal heightCm,
        BigDecimal weightKg,
        String goal,
        String activityLevel,
        String dietaryPreferences,
        String allergies
) {
    public static ProfileDto from(User user, ClientProfile profile) {
        return new ProfileDto(
                user.getEmail(),
                user.getPrenom(),
                user.getNom(),
                List.of("ROLE_CLIENT"),
                profile == null ? null : profile.getGender(),
                profile == null ? null : profile.getBirthDate(),
                profile == null ? null : profile.getHeightCm(),
                profile == null ? null : profile.getWeightKg(),
                profile == null ? null : profile.getGoal(),
                profile == null ? null : profile.getActivityLevel(),
                profile == null ? null : profile.getDietaryPreferences(),
                profile == null ? null : profile.getAllergies()
        );
    }
}
