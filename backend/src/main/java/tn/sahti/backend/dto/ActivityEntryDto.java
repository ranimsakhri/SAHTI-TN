package tn.sahti.backend.dto;

import tn.sahti.backend.entity.ActivityEntry;

import java.time.LocalDate;

public record ActivityEntryDto(
        Long id,
        String type,
        LocalDate date,
        int durationMinutes,
        String intensity,
        String notes
) {
    public static ActivityEntryDto from(ActivityEntry entry) {
        return new ActivityEntryDto(
                entry.getId(),
                entry.getType(),
                entry.getDate(),
                entry.getDurationMinutes(),
                entry.getIntensity().name(),
                entry.getNotes()
        );
    }
}
