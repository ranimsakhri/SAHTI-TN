package tn.rnu.isetr.sahtitn_mobile.models

data class CreateMealRequest(
    val foodId: Long,
    val type: String,
    val date: String,
    val quantityGrams: Double
)