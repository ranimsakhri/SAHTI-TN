
package tn.rnu.isetr.sahtitn_mobile.activities

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityAjouterActiviteBinding
import tn.rnu.isetr.sahtitn_mobile.models.ActiviteRequest
import tn.rnu.isetr.sahtitn_mobile.repositories.ActiviteRepository
import tn.rnu.isetr.sahtitn_mobile.repositories.ProfilRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodel.ActiviteViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class AjouterActiviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjouterActiviteBinding
    private lateinit var activiteViewModel: ActiviteViewModel

    private var poidsUtilisateur = 0.0

    private val activites = listOf(
        "Marche",
        "Course à pied",
        "Vélo",
        "Natation",
        "Musculation",
        "Yoga",
        "Danse"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAjouterActiviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser le ViewModel
        val repository = ActiviteRepository(
            applicationContext,
            SessionManager(applicationContext)
        )

        activiteViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ActiviteViewModel(repository) as T
                }
            }
        )[ActiviteViewModel::class.java]

        // Observer les messages
        activiteViewModel.message.observe(this) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // Gérer le chargement
        activiteViewModel.chargement.observe(this) { chargement ->
            binding.btnEnregistrerActivite.isEnabled = !chargement
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        // Types d'activités
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            activites
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerTypeActivite.adapter = adapter

        // Récupérer le poids depuis le profil santé
        recupererPoidsUtilisateur()

        // Recalculer lorsque le type change
        binding.spinnerTypeActivite.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    actualiserCalories()
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {}
            }
        )

        // Recalculer lorsque la durée change
        binding.etDuree.addTextChangedListener(
            object : android.text.TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    actualiserCalories()
                }

                override fun afterTextChanged(
                    s: android.text.Editable?
                ) {}
            }
        )

        binding.btnEnregistrerActivite.setOnClickListener {
            enregistrerActivite()
        }
    }

    private fun recupererPoidsUtilisateur() {

        val sessionManager = SessionManager(applicationContext)
        val repository = ProfilRepository(
            applicationContext,
            sessionManager
        )

        lifecycleScope.launch {

            val resultat = repository.recupererProfil()
            val profil = resultat.getOrNull()

            if (profil != null) {
                poidsUtilisateur = profil.poids
                actualiserCalories()
            } else {
                Toast.makeText(
                    this@AjouterActiviteActivity,
                    "Impossible de récupérer votre poids. Vérifiez votre profil santé.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun obtenirMET(type: String): Double {
        return when (type) {
            "Marche" -> 3.5
            "Course à pied" -> 8.0
            "Vélo" -> 6.0
            "Natation" -> 7.0
            "Musculation" -> 5.0
            "Yoga" -> 2.5
            "Danse" -> 5.5
            else -> 3.0
        }
    }

    private fun calculerCalories(duree: Int, type: String): Int {
        val met = obtenirMET(type)

        return (
                met * 3.5 * poidsUtilisateur / 200 * duree
                ).roundToInt()
    }

    private fun actualiserCalories() {

        val duree = binding.etDuree.text.toString().toIntOrNull()

        if (
            poidsUtilisateur > 0 &&
            duree != null &&
            duree > 0 &&
            binding.spinnerTypeActivite.selectedItem != null
        ) {
            val type = binding.spinnerTypeActivite.selectedItem.toString()
            val calories = calculerCalories(duree, type)

            binding.tvCaloriesEstimees.text =
                "Calories dépensées estimées : $calories kcal"
        } else {
            binding.tvCaloriesEstimees.text =
                "Les calories dépensées seront calculées automatiquement."
        }
    }

    private fun enregistrerActivite() {

        val duree = binding.etDuree.text.toString().toIntOrNull()

        if (duree == null || duree <= 0 || duree > 1440) {
            binding.etDuree.error =
                "Saisissez une durée entre 1 et 1440 minutes"
            binding.etDuree.requestFocus()
            return
        }

        if (poidsUtilisateur <= 0) {
            Toast.makeText(
                this,
                "Votre poids est nécessaire. Complétez votre profil santé.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val type = binding.spinnerTypeActivite.selectedItem.toString()
        val calories = calculerCalories(duree, type)

        // Préparer les données à envoyer au backend
        val request = ActiviteRequest(
            type = type,
            duree = duree,
            caloriesDepensees = calories
        )

        // Enregistrer dans MySQL
        activiteViewModel.ajouterActivite(request) { succes ->

            if (succes) {

                val resultat = intent.apply {
                    putExtra("typeActivite", type)
                    putExtra("duree", duree)
                    putExtra("caloriesDepensees", calories)
                }

                setResult(Activity.RESULT_OK, resultat)
                finish()
            }
        }
    }
}
