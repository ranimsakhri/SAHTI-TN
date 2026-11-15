package tn.rnu.isetr.sahtitn_mobile.repository

import tn.rnu.isetr.sahtitn_mobile.api.FoodApi
import tn.rnu.isetr.sahtitn_mobile.models.FoodDto

class FoodRepository(
    private val foodApi: FoodApi
) {

    suspend fun recupererAliments() =
        foodApi.recupererAliments()

    suspend fun rechercherAliments(
        q: String? = null,
        category: String? = null
    ) = foodApi.rechercherAliments(q, category)
}