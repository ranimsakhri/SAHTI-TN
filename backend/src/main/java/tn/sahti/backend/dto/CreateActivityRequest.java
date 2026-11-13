package tn.sahti.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.sahti.backend.entity.ActivityIntensity;

import java.time.LocalDate;

public record CreateActivityRequest(
        @NotBlank @Size(max = 80) String type,
        @NotNull LocalDate date,
        @Min(1) @Max(1440) int durationMinutes,
        @NotNull ActivityIntensity intensity,
        @Size(max = 500) String notes
) {
}
