
package tn.rnu.isetr.sahtitn_mobile.models

data class ProfilResponse(
    val id: Long,
    val dateNaissance: String,
    val sexe: String,
    val taille: Double,
    val poids: Double,
    val objectif: String,
    val niveauActivite: String,
    val preferencesAlimentaires: String? = null,
    val allergiesIntolerances: String? = null,
    val message: String? = null
)
