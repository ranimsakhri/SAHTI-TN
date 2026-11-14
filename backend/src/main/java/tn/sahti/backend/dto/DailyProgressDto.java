package tn.sahti.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyProgressDto(
        LocalDate date,
        BigDecimal caloriesEstimated,
        int mealEntries,
        int activeMinutes,
        int activities
) {
}
