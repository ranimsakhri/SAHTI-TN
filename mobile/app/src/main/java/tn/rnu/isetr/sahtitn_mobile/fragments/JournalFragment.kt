
package tn.rnu.isetr.sahtitn_mobile.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import tn.rnu.isetr.sahtitn_mobile.activities.AjouterActiviteActivity
import tn.rnu.isetr.sahtitn_mobile.activities.AjouterRepasActivity
import tn.rnu.isetr.sahtitn_mobile.api.RetrofitClient
import tn.rnu.isetr.sahtitn_mobile.databinding.FragmentJournalBinding
import tn.rnu.isetr.sahtitn_mobile.models.MealEntryDto
import tn.rnu.isetr.sahtitn_mobile.models.ActiviteResponse
import tn.rnu.isetr.sahtitn_mobile.repositories.ActiviteRepository
import tn.rnu.isetr.sahtitn_mobile.repository.MealRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.MealViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.MealViewModelFactory
import tn.rnu.isetr.sahtitn_mobile.viewmodel.ActiviteViewModel
import java.time.LocalDate
import java.util.Locale
import java.time.format.DateTimeFormatter
import android.app.DatePickerDialog

class JournalFragment : Fragment() {

    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealViewModel: MealViewModel
    private lateinit var activiteViewModel: ActiviteViewModel

    private val listeRepas = mutableListOf<MealEntryDto>()
    private val listeActivites = mutableListOf<ActiviteResponse>()
    private var dateSelectionnee: LocalDate = LocalDate.now()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateFormatee = LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH)
        )

        binding.tvDate.text = "Aujourd'hui • $dateFormatee"
        val sessionManager = SessionManager(requireContext())

        // Initialiser Retrofit
        RetrofitClient.init(requireContext(), sessionManager)

        // Initialiser le ViewModel des repas
        val mealRepository = MealRepository(RetrofitClient.mealApi)

        mealViewModel = ViewModelProvider(
            this,
            MealViewModelFactory(mealRepository)
        )[MealViewModel::class.java]

        // Initialiser le ViewModel des activités
        val activiteRepository = ActiviteRepository(
            requireContext(),
            sessionManager
        )

        activiteViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    @Suppress("UNCHECKED_CAST")
                    return ActiviteViewModel(activiteRepository) as T
                }
            }
        )[ActiviteViewModel::class.java]

        // Observer les repas
        mealViewModel.mealsResult.observe(viewLifecycleOwner) { result ->

            result.onSuccess { repas ->
                listeRepas.clear()
                listeRepas.addAll(repas)
                actualiserJournal()
            }

            result.onFailure { erreur ->
                Toast.makeText(
                    requireContext(),
                    "Erreur de chargement des repas : ${erreur.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Observer les opérations sur les repas
        mealViewModel.operationResult.observe(viewLifecycleOwner) { result ->

            result.onSuccess { message ->
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            result.onFailure { erreur ->
                Toast.makeText(
                    requireContext(),
                    erreur.message ?: "Une erreur est survenue",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Observer les activités
        activiteViewModel.activites.observe(viewLifecycleOwner) { activites ->
            listeActivites.clear()
            listeActivites.addAll(activites)
            actualiserActivites()
        }

        // Observer les messages des activités
        activiteViewModel.message.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Charger les données
        mealViewModel.chargerRepas(dateSelectionnee.toString())
        activiteViewModel.recupererActivites()

        // Bouton Ajouter repas
        binding.btnAjouterRepas.setOnClickListener {
            val intent = Intent(
                requireContext(),
                AjouterRepasActivity::class.java
            )
            ajouterRepasLauncher.launch(intent)
        }

        // Bouton Ajouter activité
        binding.btnAjouterActivite.setOnClickListener {
            val intent = Intent(
                requireContext(),
                AjouterActiviteActivity::class.java
            )
            ajouterActiviteLauncher.launch(intent)
        }

        binding.btnChoisirDate.setOnClickListener {
            afficherSelecteurDate()
        }
    }

    // Actualiser l'affichage des repas
    private fun actualiserJournal() {

        // Calculer la somme des calories de tous les repas
        val totalCalories = listeRepas.sumOf {
            it.caloriesEstimated
        }

        // Nombre de repas enregistrés
        val nombreRepas = listeRepas.size

        // Afficher le total calorique
        binding.tvCalories.text =
            "${String.format(Locale.FRANCE, "%.0f", totalCalories)} kcal"

        // Afficher le nombre de repas
        binding.tvRepas.text =
            if (nombreRepas <= 1) {
                "$nombreRepas repas enregistré"
            } else {
                "$nombreRepas repas enregistrés"
            }

        binding.layoutListeRepas.removeAllViews()

        if (listeRepas.isEmpty()) {
            val message = TextView(requireContext()).apply {
                text = "Aucun repas enregistré pour le moment."
                textSize = 14f
                setTextColor(Color.GRAY)
                setPadding(0, 12, 0, 12)
            }

            binding.layoutListeRepas.addView(message)
            return
        }
val typesRepas = listOf(
            "PETIT_DEJEUNER" to "Petit-déjeuner",
            "DEJEUNER" to " Déjeuner",
            "DINER" to "Dîner",
            "COLLATION" to "Collation"
        )

        typesRepas.forEach { (type, titre) ->

            val repasDuType = listeRepas.filter {
                it.type == type
            }

            if (repasDuType.isNotEmpty()) {

                // Titre de la catégorie
                val titreCategorie = TextView(requireContext()).apply {
                    text = titre
                    textSize = 18f
                    setTextColor(Color.parseColor("#1E4D3A"))
                    setPadding(4, 20, 4, 8)
                }

                binding.layoutListeRepas.addView(titreCategorie)

                // Afficher les repas de cette catégorie
                repasDuType.forEach { repas ->

                    val ligne = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_VERTICAL
                        setPadding(12, 12, 8, 12)
                        setBackgroundColor(Color.WHITE)
                    }

                    val details = TextView(requireContext()).apply {
                        text = "${repas.foodName} — ${repas.quantityGrams} g\n" +
                                "${String.format(Locale.FRANCE, "%.1f", repas.caloriesEstimated)} kcal"

                        textSize = 14f
                        setTextColor(Color.rgb(30, 77, 58))
                        setPadding(4, 4, 4, 4)
                    }

                    val boutonSupprimer = Button(requireContext()).apply {
                        text = "Supprimer"
                        contentDescription = "Supprimer ce repas"
                        setTextColor(Color.parseColor("#1E4D3A"))
                        setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(Color.TRANSPARENT)
                        )
                        textSize = 12f
                    }

                    ligne.addView(
                        details,
                        LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                    )

                    ligne.addView(boutonSupprimer)

                    binding.layoutListeRepas.addView(ligne)

                    boutonSupprimer.setOnClickListener {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Supprimer le repas")
                            .setMessage(
                                "Voulez-vous vraiment supprimer ${repas.foodName} ?"
                            )
                            .setPositiveButton("Supprimer") { _, _ ->
                                mealViewModel.supprimerRepas(
                                    repas.id,
                                    dateSelectionnee.toString()
                                )
                            }
                            .setNegativeButton("Annuler", null)
                            .show()
                    }
                }
            }
        }
    }

    // Actualiser l'affichage des activités
    private fun actualiserActivites() {

        val total = listeActivites.sumOf {
            it.caloriesDepensees
        }

        binding.tvCaloriesDepensees.text = "$total kcal"

        binding.layoutListeActivites.removeAllViews()

        if (listeActivites.isEmpty()) {
            val message = TextView(requireContext()).apply {
                text = "Aucune activité enregistrée pour le moment."
                textSize = 14f
                setTextColor(Color.GRAY)
                setPadding(12, 12, 12, 12)
            }

            binding.layoutListeActivites.addView(message)
            return
        }

        listeActivites.forEach { activite ->

            val ligne = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(12, 12, 8, 12)
                setBackgroundColor(Color.WHITE)
            }

            val details = TextView(requireContext()).apply {
                text = "${activite.type}\n" +
                        "Durée : ${activite.duree} minutes\n" +
                        "Calories dépensées : ${activite.caloriesDepensees} kcal"

                textSize = 14f
                setTextColor(Color.parseColor("#1E4D3A"))
                setPadding(4, 4, 4, 4)
            }

            val boutonSupprimer = Button(requireContext()).apply {
                text = "Supprimer"
                contentDescription = "Supprimer cette activité"
                setTextColor(Color.parseColor("#1E4D3A"))
                setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                        Color.TRANSPARENT
                    )
                )
                textSize = 12f
            }

            ligne.addView(
                details,
                LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            )

            ligne.addView(boutonSupprimer)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.bottomMargin = 12
            ligne.layoutParams = params

            binding.layoutListeActivites.addView(ligne)

            // Supprimer l'activité
            boutonSupprimer.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Supprimer l'activité")
                    .setMessage(
                        "Voulez-vous vraiment supprimer cette activité ?"
                    )
                    .setPositiveButton("Supprimer") { _, _ ->
                        activiteViewModel.supprimerActivite(activite.id)
                    }
                    .setNegativeButton("Annuler", null)
                    .show()
            }
        }
    }

    // Résultat de l'ajout d'un repas
    private val ajouterRepasLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                mealViewModel.chargerRepas(dateSelectionnee.toString())
            }
        }

    // Résultat de l'ajout d'une activité
    private val ajouterActiviteLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                activiteViewModel.recupererActivites()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun afficherSelecteurDate() {

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, annee, mois, jour ->

                dateSelectionnee = LocalDate.of(
                    annee,
                    mois + 1,
                    jour
                )

                val dateApi = dateSelectionnee.toString()

                val format = DateTimeFormatter.ofPattern(
                    "EEEE d MMMM yyyy",
                    Locale.FRENCH
                )

                binding.tvDate.text =
                    dateSelectionnee.format(format)

                // Charger les repas de la date sélectionnée
                mealViewModel.chargerRepas(dateApi)
            },
            dateSelectionnee.year,
            dateSelectionnee.monthValue - 1,
            dateSelectionnee.dayOfMonth
        )

        datePicker.datePicker.maxDate = System.currentTimeMillis()

        datePicker.show()
    }
}
