
package tn.rnu.isetr.sahtitn_mobile.repositories

import android.content.Context
import tn.rnu.isetr.sahtitn_mobile.api.RetrofitClient
import tn.rnu.isetr.sahtitn_mobile.models.AuthResponse
import tn.rnu.isetr.sahtitn_mobile.models.ChangePasswordRequest
import tn.rnu.isetr.sahtitn_mobile.models.ForgotPasswordRequest
import tn.rnu.isetr.sahtitn_mobile.models.RegisterRequest
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.models.LoginRequest
import tn.rnu.isetr.sahtitn_mobile.models.ResetPasswordRequest

class AuthRepository(
    context: Context,
    private val sessionManager: SessionManager
) {
    init {
        RetrofitClient.init(context,sessionManager)
    }

    private val authApi = RetrofitClient.authApi

    suspend fun register(
        request: RegisterRequest
    ): Result<AuthResponse> {

        return try {
            val response = authApi.register(request)

            if (response.isSuccessful) {

                val result = response.body()

                if (result != null) {

                    // Sauvegarder la session après inscription
                    sessionManager.saveSession(result)

                    Result.success(result)

                } else {
                    Result.failure(
                        Exception("Réponse vide du serveur")
                    )
                }

            } else {

                val message = response.errorBody()?.string()
                    ?: "Erreur lors de l'inscription"

                Result.failure(Exception(message))
            }

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    "Impossible de contacter le serveur : ${e.message}"
                )
            )
        }
    }
    suspend fun login(
        request: LoginRequest
    ): Result<AuthResponse> {

        return try {
            val response = authApi.login(request)

            if (response.isSuccessful) {

                val result = response.body()

                if (result != null) {

                    // Sauvegarder la session après connexion
                    sessionManager.saveSession(result)

                    Result.success(result)

                } else {
                    Result.failure(
                        Exception("Réponse vide du serveur")
                    )
                }

            } else {

                val message = response.errorBody()?.string()
                    ?: "Erreur lors de la connexion"

                Result.failure(Exception(message))
            }

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    "Impossible de contacter le serveur : ${e.message}"
                )
            )
        }
    }

    fun getToken(): String? {
        return sessionManager.getToken()
    }

    fun isLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun logout() {
        sessionManager.clearSession()
    }

    suspend fun changePassword(
        request: ChangePasswordRequest
    ): Result<String> {
        return try {
            val response = RetrofitClient.authApi.changePassword(request)

            if (response.isSuccessful) {
                Result.success(
                    response.body()?.get("message")
                        ?: "Mot de passe modifié avec succès."
                )
            } else {
                Result.failure(
                    Exception("Erreur : ${response.errorBody()?.string()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(
        request: ResetPasswordRequest
    ): Result<String> {
        return try {
            val response = RetrofitClient.authApi.resetPassword(request)

            if (response.isSuccessful) {
                val message = response.body()?.get("message")
                    ?: "Mot de passe réinitialisé avec succès."

                Result.success(message)
            } else {
                val erreur = response.errorBody()?.string()
                    ?: "Erreur lors de la réinitialisation du mot de passe."

                Result.failure(Exception(erreur))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun forgotPassword(
        request: ForgotPasswordRequest
    ): Result<String> {
        return try {
            val response = RetrofitClient.authApi.forgotPassword(request)

            if (response.isSuccessful) {
                val message = response.body()?.get("message")
                    ?: "Si cet email existe, un lien de réinitialisation a été envoyé."

                Result.success(message)
            } else {
                val erreur = response.errorBody()?.string()
                    ?: "Erreur lors de l'envoi du lien."

                Result.failure(Exception(erreur))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
