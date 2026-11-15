package tn.rnu.isetr.sahtitn_mobile.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import tn.rnu.isetr.sahtitn_mobile.models.CreateMealRequest
import tn.rnu.isetr.sahtitn_mobile.models.MealEntryDto

interface MealApi {

    // Récupérer les repas d'une journée
    @GET("api/meals")
    suspend fun recupererRepas(
        @Query("date") date: String? = null
    ): Response<List<MealEntryDto>>

    // Ajouter un repas
    @POST("api/meals")
    suspend fun ajouterRepas(
        @Body request: CreateMealRequest
    ): Response<MealEntryDto>

    // Supprimer un repas
    @DELETE("api/meals/{id}")
    suspend fun supprimerRepas(
        @Path("id") id: Long
    ): Response<Unit>
}