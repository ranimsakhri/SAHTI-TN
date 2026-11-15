
package tn.rnu.isetr.sahtitn_mobile.repositories

import ProfilRequest
import android.content.Context
import tn.rnu.isetr.sahtitn_mobile.api.RetrofitClient
//import tn.rnu.isetr.sahtitn_mobile.models.ProfilRequest
import tn.rnu.isetr.sahtitn_mobile.models.ProfilResponse
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager

class ProfilRepository(
    context: Context,
    sessionManager: SessionManager
) {

    init {
        RetrofitClient.init(context, sessionManager)
    }

    private val profilApi = RetrofitClient.profilApi

    suspend fun enregistrerProfil(
        request: ProfilRequest
    ): Result<ProfilResponse> {

        return try {
            val response = profilApi.enregistrerProfil(request)

            if (response.isSuccessful) {
                val profil = response.body()

                if (profil != null) {
                    Result.success(profil)
                } else {
                    Result.failure(Exception("Réponse vide du serveur"))
                }
            } else {
                val message = response.errorBody()?.string()
                    ?: "Erreur lors de l'enregistrement du profil"

                Result.failure(Exception(message))
            }

        } catch (e: Exception) {
            Result.failure(
                Exception("Impossible de contacter le serveur : ${e.message}")
            )
        }
    }
    suspend fun recupererProfil(): Result<ProfilResponse> {

        return try {
            val response = profilApi.recupererProfil()

            if (response.isSuccessful) {
                val profil = response.body()

                if (profil != null) {
                    Result.success(profil)
                } else {
                    Result.failure(Exception("Réponse vide du serveur"))
                }
            } else {
                val message = response.errorBody()?.string()
                    ?: "Erreur lors de la récupération du profil"

                Result.failure(Exception(message))
            }

        } catch (e: Exception) {
            Result.failure(
                Exception("Impossible de contacter le serveur : ${e.message}")
            )
        }
    }
}
