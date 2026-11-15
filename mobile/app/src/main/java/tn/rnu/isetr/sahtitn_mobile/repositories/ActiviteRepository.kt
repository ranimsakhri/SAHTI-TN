
package tn.rnu.isetr.sahtitn_mobile.repositories

import android.content.Context
import tn.rnu.isetr.sahtitn_mobile.api.RetrofitClient
import tn.rnu.isetr.sahtitn_mobile.models.ActiviteRequest
import tn.rnu.isetr.sahtitn_mobile.models.ActiviteResponse
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager

class ActiviteRepository(
    context: Context,
    sessionManager: SessionManager
) {
    init {
        RetrofitClient.init(context, sessionManager)
    }

    suspend fun ajouterActivite(request: ActiviteRequest): Result<ActiviteResponse> {
        return try {
            val response = RetrofitClient.activiteApi.ajouterActivite(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Erreur lors de l'ajout de l'activité : ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recupererActivites(): Result<List<ActiviteResponse>> {
        return try {
            val response = RetrofitClient.activiteApi.recupererActivites()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("Erreur lors de la récupération des activités : ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun supprimerActivite(id: Long): Result<Unit> {
        return try {
            val response = RetrofitClient.activiteApi.supprimerActivite(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception("Erreur lors de la suppression : ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
