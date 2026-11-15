
package tn.rnu.isetr.sahtitn_mobile.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tn.rnu.isetr.sahtitn_mobile.models.ActiviteRequest
import tn.rnu.isetr.sahtitn_mobile.models.ActiviteResponse

interface ActiviteApi {

    @POST("api/activites")
    suspend fun ajouterActivite(
        @Body request: ActiviteRequest
    ): Response<ActiviteResponse>

    @GET("api/activites")
    suspend fun recupererActivites(): Response<List<ActiviteResponse>>

    @DELETE("api/activites/{id}")
    suspend fun supprimerActivite(
        @Path("id") id: Long
    ): Response<Unit>
}
