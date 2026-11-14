package tn.sahti.backend.dto;

import org.junit.jupiter.api.Test;
import tn.sahti.backend.entity.Food;
import tn.sahti.backend.entity.MealEntry;
import tn.sahti.backend.entity.MealType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MealEntryDtoTest {

    @Test
    void estimatesCaloriesFromFoodWeightAndPerHundredGramValue() {
        Food food = Food.builder()
                .id(3L)
                .name("Lablabi")
                .caloriesPer100g(BigDecimal.valueOf(125))
                .build();
        MealEntry entry = MealEntry.builder()
                .id(8L)
                .food(food)
                .type(MealType.DEJEUNER)
                .date(LocalDate.of(2026, 11, 10))
                .quantityGrams(BigDecimal.valueOf(240))
                .build();

        assertEquals(BigDecimal.valueOf(300), MealEntryDto.from(entry).caloriesEstimated());
    }
}
