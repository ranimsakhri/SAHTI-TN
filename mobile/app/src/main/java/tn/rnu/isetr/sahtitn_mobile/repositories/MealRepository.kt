package tn.rnu.isetr.sahtitn_mobile.repository

import tn.rnu.isetr.sahtitn_mobile.api.MealApi
import tn.rnu.isetr.sahtitn_mobile.models.CreateMealRequest

class MealRepository(
    private val mealApi: MealApi
) {

    suspend fun recupererRepas(date: String? = null) =
        mealApi.recupererRepas(date)

    suspend fun ajouterRepas(request: CreateMealRequest) =
        mealApi.ajouterRepas(request)

    suspend fun supprimerRepas(id: Long) =
        mealApi.supprimerRepas(id)
}