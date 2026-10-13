package tn.sahti.backend.dto;

import java.time.LocalDateTime;

public record ProfessionalApplicationDto(
        Long id,
        String nom,
        String prenom,
        String email,
        String type,
        String specialite,
        String bio,
        String status,
        String motifRefus,
        LocalDateTime submittedAt
) {}