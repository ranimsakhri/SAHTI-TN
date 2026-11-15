package tn.rnu.isetr.sahtitn_mobile.models

data class AuthResponse(
    val token: String,
    val role: String,
    val nom: String,
    val prenom: String,
    val email: String
)