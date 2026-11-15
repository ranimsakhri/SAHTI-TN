
package tn.rnu.isetr.sahtitn_mobile.models

data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)
