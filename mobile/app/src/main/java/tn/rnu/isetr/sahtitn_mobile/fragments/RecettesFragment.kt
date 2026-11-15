
package tn.rnu.isetr.sahtitn_mobile.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import tn.rnu.isetr.sahtitn_mobile.R
import tn.rnu.isetr.sahtitn_mobile.models.Recette
import android.widget.EditText
import android.widget.Button
import android.text.Editable
import android.text.TextWatcher
import java.text.Normalizer
class RecettesFragment : Fragment(R.layout.activity_recettes_fragment) {

    private lateinit var layoutListeRecettes: LinearLayout
    private lateinit var etRecherche: EditText

    private var categorieSelectionnee = "Tout"
    private var texteRecherche = ""
    private val listeRecettes = listOf(

        // Anciennes recettes tunisiennes
        Recette(
            "Ojja tunisienne", "Plat", 340,
            R.drawable.ojja_tunisienne,
            listOf("Œufs", "Tomates", "Poivrons", "Ail", "Huile d'olive"),
            "Faire revenir les légumes et les épices, ajouter les œufs et laisser cuire.",
            18
        ),

        Recette(
            "Lablabi", "Plat", 380,
            R.drawable.lablabi,
            listOf("Pois chiches", "Pain", "Ail", "Cumin", "Harissa"),
            "Cuire les pois chiches, préparer le bouillon et servir avec le pain.",
            17
        ),

        Recette(
            "Couscous tunisien", "Plat", 465,
            R.drawable.couscous_tunisien,
            listOf("Semoule", "Poulet", "Carottes", "Pommes de terre", "Pois chiches"),
            "Préparer la sauce, cuire les légumes et le poulet, puis servir avec la semoule.",
            28
        ),

        Recette(
            "Slata mechouia", "Entrée", 185,
            R.drawable.slata_mechouia,
            listOf("Poivrons", "Tomates", "Ail", "Huile d'olive", "Thon"),
            "Griller les légumes, les éplucher et les écraser. Ajouter les garnitures.",
            9
        ),

        Recette(
            "Brik tunisienne", "Entrée", 290,
            R.drawable.brik_tunisienne,
            listOf("Feuille de malsouka", "Œuf", "Thon", "Persil"),
            "Garnir la feuille, ajouter l'œuf et faire cuire.",
            14
        ),

        Recette(
            "Salade tunisienne", "Entrée", 160,
            R.drawable.salade_tunisienne,
            listOf("Tomates", "Concombre", "Oignon", "Thon", "Œuf"),
            "Couper les légumes et ajouter le thon et l'œuf.",
            12
        ),

        // Nouvelles recettes healthy
        Recette(
            "Poulet grillé au quinoa", "Plat", 420,
            R.drawable.poulet_quinoa,
            listOf("Poulet", "Quinoa", "Courgettes", "Brocoli", "Huile d'olive"),
            "Griller le poulet, cuire le quinoa et accompagner de légumes.",
            38
        ),

        Recette(
            "Salade de thon aux pois chiches", "Entrée", 350,
            R.drawable.salade_thon,
            listOf("Thon", "Pois chiches", "Tomates", "Concombre", "Œuf"),
            "Mélanger les ingrédients et assaisonner avec du citron.",
            30
        ),

        Recette(
            "Omelette aux épinards", "Petit-déjeuner", 280,
            R.drawable.omelette_epinards,
            listOf("Œufs", "Épinards", "Fromage", "Poivre"),
            "Faire revenir les épinards, ajouter les œufs battus et le fromage.",
            21
        ),

        Recette(
            "Sardines grillées", "Plat", 320,
            R.drawable.sardines_grillees,
            listOf("Sardines", "Citron", "Ail", "Persil"),
            "Mariner les sardines avec les épices et les griller.",
            32
        ),

        Recette(
            "Soupe de lentilles", "Plat", 300,
            R.drawable.soupe_lentilles,
            listOf("Lentilles", "Carottes", "Oignon", "Ail", "Cumin"),
            "Cuire les lentilles avec les légumes et mixer selon la préférence.",
            18
        ),

        Recette(
            "Yaourt grec aux amandes", "Dessert", 250,
            R.drawable.yaourt_amandes,
            listOf("Yaourt grec", "Amandes", "Banane", "Graines de chia"),
            "Mettre le yaourt dans un bol et ajouter les fruits et les amandes.",
            20
        ),
        // Recettes pour enfants
        Recette(
            "Mini-pizza maison",
            "Plats enfants",
            280,
            R.drawable.mini_pizza,
            listOf("Pâte à pizza", "Tomate", "Fromage", "Poulet"),
            "Garnir la pâte avec la tomate, le fromage et le poulet. Cuire au four.",
            12
        ),

        Recette(
            "Nuggets de poulet maison",
            "Plats enfants",
            320,
            R.drawable.nuggets_poulet,
            listOf("Poulet", "Œuf", "Chapelure", "Épices douces"),
            "Découper le poulet, le paner et le cuire au four.",
            25
        ),

        Recette(
            "Gratin de pommes de terre",
            "Plats enfants",
            250,
            R.drawable.gratin_pommes_terre,
            listOf("Pommes de terre", "Lait", "Fromage", "Œuf"),
            "Couper les pommes de terre, ajouter le mélange et faire gratiner au four.",
            9
        ),

        Recette(
            "Boulettes de poulet aux légumes",
            "Plats enfants",
            290,
            R.drawable.boulettes_poulet,
            listOf("Poulet haché", "Carottes", "Courgettes", "Œuf"),
            "Mélanger les ingrédients, former des boulettes et les cuire au four.",
            24
        ),

        Recette(
            "Pancakes à la banane",
            "Plats enfants",
            230,
            R.drawable.pancakes_banane,
            listOf("Banane", "Œuf", "Farine", "Lait"),
            "Écraser la banane, mélanger les ingrédients et cuire les pancakes dans une poêle.",
            8
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutListeRecettes = view.findViewById(R.id.layoutListeRecettes)
        etRecherche = view.findViewById(R.id.etRechercheRecette)

        configurerRecherche()
        configurerFiltres()

        appliquerFiltres()
    }

    private fun afficherRecettes(recettes: List<Recette>) {

        layoutListeRecettes.removeAllViews()
        if (recettes.isEmpty()) {
            val message = TextView(requireContext()).apply {
                text = "Aucune recette trouvée."
                textSize = 16f
                setTextColor(Color.GRAY)
                gravity = Gravity.CENTER
                setPadding(12, 30, 12, 30)
            }

            layoutListeRecettes.addView(message)
            return
        }
        recettes.forEach { recette ->

            val carte = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(12, 12, 12, 12)

                background = GradientDrawable().apply {
                    setColor(Color.WHITE)
                    cornerRadius = 20f
                }

                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.bottomMargin = 16
                layoutParams = params
                isClickable = true
                isFocusable = true

                setOnClickListener {
                    afficherDetailsRecette(recette)
                }
            }

            val image = ImageView(requireContext()).apply {
                setImageResource(recette.imageResId)
                scaleType = ImageView.ScaleType.FIT_CENTER
                adjustViewBounds = true
                setBackgroundColor(Color.parseColor("#FFF9F0"))

                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    500
                )
            }

            val nom = TextView(requireContext()).apply {
                text = recette.nom
                textSize = 19f
                setTextColor(Color.parseColor("#1E4D3A"))
                setTypeface(null, Typeface.BOLD)
                setPadding(4, 12, 4, 4)
            }

            val calories = TextView(requireContext()).apply {
                text = " ${recette.calories} kcal / portion"
                textSize = 14f
                setTextColor(Color.DKGRAY)
                setPadding(4, 4, 4, 8)
            }
            val proteines = TextView(requireContext()).apply {
                text = " ${recette.proteines} g de protéines / portion"
                textSize = 14f
                setTextColor(Color.parseColor("#2E7D32"))
                setTypeface(null, Typeface.BOLD)
                setPadding(4, 4, 4, 8)
            }

            carte.addView(image)
            carte.addView(nom)
            carte.addView(calories)
            carte.addView(proteines)
            layoutListeRecettes.addView(carte)
        }
    }
    private fun configurerRecherche() {

        etRecherche.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
                texteRecherche = s.toString()
                appliquerFiltres()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun configurerFiltres() {

        view?.findViewById<Button>(R.id.btnTout)?.setOnClickListener {
            categorieSelectionnee = "Tout"
            appliquerFiltres()
        }

        view?.findViewById<Button>(R.id.btnPetitDejeuner)?.setOnClickListener {
            categorieSelectionnee = "Petit-déjeuner"
            appliquerFiltres()
        }

        view?.findViewById<Button>(R.id.btnEntree)?.setOnClickListener {
            categorieSelectionnee = "Entrée"
            appliquerFiltres()
        }

        view?.findViewById<Button>(R.id.btnPlat)?.setOnClickListener {
            categorieSelectionnee = "Plat"
            appliquerFiltres()
        }

        view?.findViewById<Button>(R.id.btnDessert)?.setOnClickListener {
            categorieSelectionnee = "Dessert"
            appliquerFiltres()
        }
        view?.findViewById<Button>(R.id.btnPlatEnfant)?.setOnClickListener {
            categorieSelectionnee = "Plats enfants"
            appliquerFiltres()
        }
    }

    private fun appliquerFiltres() {

        val recherche = normaliser(texteRecherche)

        val recettesFiltrees = listeRecettes.filter { recette ->

            val nom = normaliser(recette.nom)
            val categorie = normaliser(recette.categorie)
            val ingredients = normaliser(recette.ingredients.joinToString(" "))
            val preparation = normaliser(recette.preparation)

            // Recherche par nom, catégorie, ingrédients ou préparation
            val correspondRecherche =
                recherche.isEmpty() ||
                        nom.contains(recherche) ||
                        categorie.contains(recherche) ||
                        ingredients.contains(recherche) ||
                        preparation.contains(recherche) ||
                        (recherche.contains("proteine") && recette.proteines >= 20) ||
                        (recherche.contains("calorie") && recette.calories <= 300)

            // Filtre par catégorie
            val correspondCategorie =
                categorieSelectionnee == "Tout" ||
                        recette.categorie == categorieSelectionnee

            correspondRecherche && correspondCategorie
        }

        afficherRecettes(recettesFiltrees)
    }

    private fun afficherDetailsRecette(recette: Recette) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(recette.nom)
            .setMessage(
                """
            Catégorie : ${recette.categorie}

            Calories : ${recette.calories} kcal / portion

            Cette recette fait partie de notre sélection de plats tunisiens.
            """.trimIndent()
            )
            .setPositiveButton("Fermer", null)
            .show()
    }
    private fun normaliser(texte: String): String {
        return Normalizer.normalize(texte.lowercase(), Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
            .replace("œ", "oe")
            .trim()
    }
}
