package tn.rnu.isetr.sahtitn_mobile.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tn.rnu.isetr.sahtitn_mobile.models.FoodDto

interface FoodApi {

    // Récupérer la liste des aliments
    @GET("api/foods")
    suspend fun recupererAliments(): Response<List<FoodDto>>

    // Rechercher des aliments par nom ou catégorie
    @GET("api/foods")
    suspend fun rechercherAliments(
        @Query("q") q: String? = null,
        @Query("category") category: String? = null
    ): Response<List<FoodDto>>
}