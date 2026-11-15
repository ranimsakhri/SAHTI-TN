package tn.rnu.isetr.sahtitn_mobile.utils

import android.content.Context
import android.content.SharedPreferences
import tn.rnu.isetr.sahtitn_mobile.models.AuthResponse

class SessionManager(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(
            "sahti_session",
            Context.MODE_PRIVATE
        )

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_ROLE = "role"
        private const val KEY_EMAIL = "email"
        private const val KEY_NOM = "nom"
        private const val KEY_PRENOM = "prenom"
    }

    // Sauvegarder la session après une authentification réussie
    fun saveSession(response: AuthResponse) {
        preferences.edit()
            .putString(KEY_TOKEN, response.token)
            .putString(KEY_ROLE, response.role)
            .putString(KEY_EMAIL, response.email)
            .putString(KEY_NOM, response.nom)
            .putString(KEY_PRENOM, response.prenom)
            .apply()
    }

    // Récupérer le token JWT
    fun getToken(): String? {
        return preferences.getString(KEY_TOKEN, null)
    }

    // Vérifier si l'utilisateur est connecté
    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    // Récupérer le rôle
    fun getRole(): String? {
        return preferences.getString(KEY_ROLE, null)
    }

    // Déconnexion : supprimer les informations de session
    fun clearSession() {
        preferences.edit().clear().apply()
    }
}
