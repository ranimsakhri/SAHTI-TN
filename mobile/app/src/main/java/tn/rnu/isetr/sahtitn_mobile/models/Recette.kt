package tn.rnu.isetr.sahtitn_mobile.models

data class Recette(
    val nom: String,
    val categorie: String,
    val calories: Int,
    val imageResId: Int,
    val ingredients: List<String>,
    val preparation: String,
    val proteines: Int
)