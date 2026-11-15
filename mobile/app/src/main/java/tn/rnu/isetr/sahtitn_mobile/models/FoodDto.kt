package tn.rnu.isetr.sahtitn_mobile.models

data class FoodDto(
    val id: Long,
    val name: String,
    val category: String,
    val origin: String,
    val serving: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double?,
    val carbohydratesPer100g: Double?,
    val fatPer100g: Double?
)