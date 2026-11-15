package tn.rnu.isetr.sahtitn_mobile.models

data class MealEntryDto(
    val id: Long,
    val foodId: Long,
    val foodName: String,
    val type: String,
    val date: String,
    val quantityGrams: Double,
    val caloriesEstimated: Double
)