
package tn.rnu.isetr.sahtitn_mobile.models

data class ActiviteResponse(
    val id: Long,
    val type: String,
    val duree: Int,
    val caloriesDepensees: Int,
    val dateEnregistrement: String
)
