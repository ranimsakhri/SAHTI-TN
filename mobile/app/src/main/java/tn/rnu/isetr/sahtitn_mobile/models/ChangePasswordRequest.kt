package tn.rnu.isetr.sahtitn_mobile.models

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)