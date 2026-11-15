package tn.rnu.isetr.sahtitn_mobile.models

data class RegisterRequest (
    val nom: String,
    val prenom: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)