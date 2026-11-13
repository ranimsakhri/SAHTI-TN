package tn.sahti.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import tn.sahti.backend.entity.MealType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateMealRequest(
        @NotNull Long foodId,
        @NotNull MealType type,
        @NotNull LocalDate date,
        @NotNull @DecimalMin("1.0") @DecimalMax("5000.0") BigDecimal quantityGrams
) {
}
