
package tn.rnu.isetr.sahtitn_mobile.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tn.rnu.isetr.sahtitn_mobile.models.RepasRequest
import tn.rnu.isetr.sahtitn_mobile.models.RepasResponse

interface RepasApi {

    @POST("api/repas")
    suspend fun ajouterRepas(
        @Body request: RepasRequest
    ): Response<RepasResponse>

    @GET("api/repas")
    suspend fun recupererRepas(): Response<List<RepasResponse>>

    @DELETE("api/repas/{id}")
    suspend fun supprimerRepas(
        @Path("id") id: Long
    ): Response<Unit>
}
