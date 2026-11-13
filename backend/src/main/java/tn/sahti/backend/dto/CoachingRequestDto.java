package tn.sahti.backend.dto;

import tn.sahti.backend.entity.CoachingRequest;

import java.time.LocalDateTime;

public record CoachingRequestDto(
        Long id,
        String clientFirstName,
        String clientLastName,
        String clientEmail,
        String type,
        String message,
        String status,
        LocalDateTime createdAt
) {
    public static CoachingRequestDto from(CoachingRequest request) {
        return new CoachingRequestDto(
                request.getId(),
                request.getClient().getPrenom(),
                request.getClient().getNom(),
                request.getClient().getEmail(),
                request.getProfessionalType().name(),
                request.getMessage(),
                request.getStatus().name(),
                request.getCreatedAt()
        );
    }
}
