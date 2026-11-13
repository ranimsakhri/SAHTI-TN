package tn.sahti.backend.dto;

import tn.sahti.backend.entity.Food;

import java.math.BigDecimal;

public record FoodDto(
        Long id,
        String name,
        String category,
        String origin,
        String serving,
        BigDecimal caloriesPer100g,
        BigDecimal proteinPer100g,
        BigDecimal carbohydratesPer100g,
        BigDecimal fatPer100g
) {
    public static FoodDto from(Food food) {
        return new FoodDto(
                food.getId(),
                food.getName(),
                food.getCategory(),
                food.getOrigin(),
                food.getServing(),
                food.getCaloriesPer100g(),
                food.getProteinPer100g(),
                food.getCarbohydratesPer100g(),
                food.getFatPer100g()
        );
    }
}
