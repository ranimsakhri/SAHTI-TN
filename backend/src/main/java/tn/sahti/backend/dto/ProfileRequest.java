package tn.sahti.backend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProfileRequest(
        @NotBlank @Pattern(regexp = "FEMME|HOMME|AUTRE|FILLE|GARCON") String gender,
        @NotNull @Past LocalDate birthDate,
        @NotNull @DecimalMin("70.0") @DecimalMax("230.0") BigDecimal heightCm,
        @NotNull @DecimalMin("5.0") @DecimalMax("350.0") BigDecimal weightKg,
        @NotBlank @Pattern(regexp = "PERDRE|MAINTENIR|PRENDRE") String goal,
        @NotBlank @Pattern(regexp = "SEDENTAIRE|LEGER|MODERE|INTENSE|TRES_INTENSE") String activityLevel,
        @Size(max = 500) String dietaryPreferences,
        @Size(max = 500) String allergies
) {
}
