
package tn.rnu.isetr.sahtitn_mobile.api

import ProfilRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
//import tn.rnu.isetr.sahtitn_mobile.models.ProfilRequest
import tn.rnu.isetr.sahtitn_mobile.models.ProfilResponse

interface ProfilApi {
// // Enregistrer le profil
    @POST("api/profile")
    suspend fun enregistrerProfil(
        @Body request: ProfilRequest
    ): Response<ProfilResponse>

    // Récupérer le profil de l'utilisateur connecté
    @GET("api/profile/details")
    suspend fun recupererProfil(): Response<ProfilResponse>
}
