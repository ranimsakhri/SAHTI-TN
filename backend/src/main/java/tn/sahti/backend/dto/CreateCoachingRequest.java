package tn.sahti.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.sahti.backend.entity.Role;

public record CreateCoachingRequest(
        @NotNull Role type,
        @Size(max = 500) String message
) {
}
