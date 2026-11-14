package tn.sahti.backend.dto;

import tn.sahti.backend.entity.MealEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public record MealEntryDto(
        Long id,
        Long foodId,
        String foodName,
        String type,
        LocalDate date,
        BigDecimal quantityGrams,
        BigDecimal caloriesEstimated
) {
    public static MealEntryDto from(MealEntry entry) {
        BigDecimal calories = entry.getFood().getCaloriesPer100g()
                .multiply(entry.getQuantityGrams())
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        return new MealEntryDto(
                entry.getId(),
                entry.getFood().getId(),
                entry.getFood().getName(),
                entry.getType().name(),
                entry.getDate(),
                entry.getQuantityGrams(),
                calories
        );
    }
}
