
package tn.rnu.isetr.sahtitn_mobile.api

import okhttp3.Interceptor
import okhttp3.Response
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager

class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //récupère la requête initiale
        val originalRequest = chain.request()
        //récupère le JWT sauvegardé
        val token = sessionManager.getToken()

        val newRequest = if (!token.isNullOrEmpty()) {
//Authorization ajoute le token dans l'en-tête HTTP
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

        } else {
            originalRequest
        }
//envoie la requête au backend
        return chain.proceed(newRequest)
    }
}
