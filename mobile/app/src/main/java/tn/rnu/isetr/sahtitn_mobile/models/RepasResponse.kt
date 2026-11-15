
package tn.rnu.isetr.sahtitn_mobile.models

data class RepasResponse(
    val id: Long,
    val nom: String,
    val type: String,
    val calories: Int,
    val dateEnregistrement: String
)
