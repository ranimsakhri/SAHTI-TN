
package tn.rnu.isetr.sahtitn_mobile.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import tn.rnu.isetr.sahtitn_mobile.api.RetrofitClient
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityAjouterRepasBinding
import tn.rnu.isetr.sahtitn_mobile.models.CreateMealRequest
import tn.rnu.isetr.sahtitn_mobile.models.FoodDto
import tn.rnu.isetr.sahtitn_mobile.repository.FoodRepository
import tn.rnu.isetr.sahtitn_mobile.repository.MealRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.FoodViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.FoodViewModelFactory
import tn.rnu.isetr.sahtitn_mobile.viewmodels.MealViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.MealViewModelFactory
import java.time.LocalDate
import java.util.Locale

class AjouterRepasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjouterRepasBinding

    private lateinit var foodViewModel: FoodViewModel
    private lateinit var mealViewModel: MealViewModel

    private var listeAliments: List<FoodDto> = emptyList()

    private val typesRepas = listOf(
        "Petit-déjeuner",
        "Déjeuner",
        "Dîner",
        "Collation"
    )

    private val typesBackend = listOf(
        "PETIT_DEJEUNER",
        "DEJEUNER",
        "DINER",
        "COLLATION"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAjouterRepasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialiserViewModels()
        configurerSpinnerTypes()
        observerViewModels()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.spinnerAliment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    actualiserCalories()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.tvCaloriesEstimees.text =
                        "Calories estimées : 0 kcal"
                }
            }

        binding.etQuantite.setOnKeyListener { _, _, _ ->
            actualiserCalories()
            false
        }

        binding.btnEnregistrerRepas.setOnClickListener {
            enregistrerRepas()
        }

        // Charger les aliments depuis le backend
        foodViewModel.chargerAliments()
    }

    private fun initialiserViewModels() {

        val sessionManager = SessionManager(applicationContext)

        // Initialiser Retrofit avec la session utilisateur
        RetrofitClient.init(applicationContext, sessionManager)

        val foodRepository = FoodRepository(RetrofitClient.foodApi)
        val mealRepository = MealRepository(RetrofitClient.mealApi)

        foodViewModel = ViewModelProvider(
            this,
            FoodViewModelFactory(foodRepository)
        )[FoodViewModel::class.java]

        mealViewModel = ViewModelProvider(
            this,
            MealViewModelFactory(mealRepository)
        )[MealViewModel::class.java]
    }

    private fun configurerSpinnerTypes() {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            typesRepas
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerTypeRepas.adapter = adapter
    }

    private fun observerViewModels() {

        // Résultat du chargement des aliments
        foodViewModel.foodsResult.observe(this) { result ->

            result.onSuccess { aliments ->

                listeAliments = aliments

                if (aliments.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Aucun aliment disponible",
                        Toast.LENGTH_LONG
                    ).show()
                    return@onSuccess
                }

                val nomsAliments = aliments.map { it.name }

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    nomsAliments
                )

                adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )

                binding.spinnerAliment.adapter = adapter
                actualiserCalories()
            }

            result.onFailure { erreur ->
                Toast.makeText(
                    this,
                    "Erreur de chargement : ${erreur.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Résultat de l'enregistrement
        mealViewModel.operationResult.observe(this) { result ->

            result.onSuccess { message ->

                Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_SHORT
                ).show()

                setResult(RESULT_OK)
                finish()
            }

            result.onFailure { erreur ->
                Toast.makeText(
                    this,
                    "Erreur : ${erreur.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // État du chargement
        mealViewModel.isLoading.observe(this) { chargement ->
            binding.btnEnregistrerRepas.isEnabled = !chargement
        }
    }

    private fun actualiserCalories() {

        val position = binding.spinnerAliment.selectedItemPosition

        if (position !in listeAliments.indices) {
            binding.tvCaloriesEstimees.text =
                "Calories estimées : 0 kcal"
            return
        }

        val quantite = binding.etQuantite.text.toString()
            .replace(",", ".")
            .toDoubleOrNull()

        if (quantite == null || quantite <= 0) {
            binding.tvCaloriesEstimees.text =
                "Calories estimées : 0 kcal"
            return
        }

        val aliment = listeAliments[position]

        val calories = aliment.caloriesPer100g * quantite / 100.0

        binding.tvCaloriesEstimees.text =
            "Calories estimées : ${String.format(Locale.FRANCE, "%.1f", calories)} kcal"
    }

    private fun enregistrerRepas() {

        // Vérifier la quantité
        val quantiteText = binding.etQuantite.text.toString()
            .trim()
            .replace(",", ".")

        if (quantiteText.isEmpty()) {
            binding.etQuantite.error = "Saisissez une quantité"
            binding.etQuantite.requestFocus()
            return
        }

        val quantite = quantiteText.toDoubleOrNull()

        if (quantite == null || quantite <= 0 || quantite > 10000) {
            binding.etQuantite.error =
                "Saisissez une quantité entre 1 et 10000 grammes"
            binding.etQuantite.requestFocus()
            return
        }

        // Vérifier la sélection de l'aliment
        val positionAliment = binding.spinnerAliment.selectedItemPosition

        if (positionAliment !in listeAliments.indices) {
            Toast.makeText(
                this,
                "Veuillez choisir un aliment",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Récupérer l'aliment sélectionné
        val aliment = listeAliments[positionAliment]

        // Récupérer le type de repas au format attendu par Spring Boot
        val positionType = binding.spinnerTypeRepas.selectedItemPosition
        val type = typesBackend[positionType]

        // Construire la requête
        val request = CreateMealRequest(
            foodId = aliment.id,
            type = type,
            date = LocalDate.now().toString(),
            quantityGrams = quantite
        )

        // Envoyer au backend
        mealViewModel.ajouterRepas(request)
    }
}
