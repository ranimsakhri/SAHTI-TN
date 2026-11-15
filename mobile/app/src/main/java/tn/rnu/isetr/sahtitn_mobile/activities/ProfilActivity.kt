
package tn.rnu.isetr.sahtitn_mobile.activities

import ProfilRequest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityProfilBinding
//import tn.rnu.isetr.sahtitn_mobile.models.ProfilRequest
import tn.rnu.isetr.sahtitn_mobile.models.ProfilResponse
import tn.rnu.isetr.sahtitn_mobile.repositories.ProfilRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.ProfilViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.ProfilViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class ProfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding

    private var recuperationEnCours = false

    private val profilViewModel: ProfilViewModel by viewModels {
        ProfilViewModelFactory(
            ProfilRepository(
                applicationContext,
                SessionManager(applicationContext)
            )
        )
    }

    // Textes affichés dans le Spinner
    private val activiteLabels = listOf(
        "Sélectionnez votre niveau d'activité",
        "Plutôt sédentaire",
        "Léger (1 à 2 jours par semaine)",
        "Modéré (3 à 4 jours par semaine)",
        "Intense (5 à 6 jours par semaine)",
        "Très intense"
    )

    // Valeurs envoyées au backend
    private val activiteValues = listOf(
        "SEDENTAIRE",
        "LEGER",
        "MODERE",
        "INTENSE",
        "TRES_INTENSE"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Initialiser la liste déroulante
        initialiserSpinner()

        // 2. Bouton Retour
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 3. Changer le mot de passe
        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(
                this,
                ChangePasswordActivity::class.java
            )
            startActivity(intent)
        }

        // 4. Observer le résultat du profil
        profilViewModel.profilResult.observe(this) { result ->

            result.onSuccess { response ->

                if (recuperationEnCours) {

                    afficherProfil(response)
                    recuperationEnCours = false

                    Toast.makeText(
                        this,
                        "Profil récupéré avec succès",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        this,
                        response.message ?: "Profil enregistré avec succès",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            result.onFailure { error ->

                if (recuperationEnCours) {

                    recuperationEnCours = false

                    val message = error.message ?: ""

                    if (
                        message.contains("404") ||
                        message.contains(
                            "Profil non trouvé",
                            ignoreCase = true
                        )
                    ) {
                        Toast.makeText(
                            this,
                            "Aucun profil enregistré. Complétez le formulaire.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Erreur de récupération : $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this,
                        error.message ?: "Erreur lors de l'enregistrement",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // 5. Gérer le chargement
        profilViewModel.isLoading.observe(this) { loading ->

            binding.btnSaveProfil.isEnabled = !loading

            binding.btnSaveProfil.text =
                if (loading) "Enregistrement..."
                else "Continuer"
        }

        // 6. Bouton Continuer
        binding.btnSaveProfil.setOnClickListener {
            enregistrerProfil()
        }

        // 7. Déconnexion
        binding.btnLogout.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui") { _, _ ->

                    SessionManager(this).clearSession()

                    val intent = Intent(
                        this,
                        LoginActivity::class.java
                    )

                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                }
                .setNegativeButton("Annuler", null)
                .show()
        }

        // 8. Récupérer automatiquement le profil existant
        recuperationEnCours = true
        profilViewModel.recupererProfil()
    }

    // Initialiser le Spinner
    private fun initialiserSpinner() {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            activiteLabels
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spNiveauActivite.adapter = adapter
        binding.spNiveauActivite.setSelection(0)
    }

    // Convertir la valeur backend en position du Spinner
    private fun positionActivite(valeur: String): Int {

        return when (valeur.uppercase().trim()) {

            "SEDENTAIRE", "FAIBLE",
            "PLUTOT SEDENTAIRE" -> 1

            "LEGER" -> 2

            "MODERE", "MODÉRÉ" -> 3

            "INTENSE", "ELEVE", "ÉLEVÉ" -> 4

            "TRES_INTENSE", "TRÈS INTENSE" -> 5

            else -> 0
        }
    }

    // Afficher les informations récupérées
    private fun afficherProfil(profil: ProfilResponse) {

        // Date de naissance
        val date = LocalDate.parse(profil.dateNaissance)

        val formatter = DateTimeFormatter.ofPattern(
            "dd/MM/uuuu",
            Locale.FRANCE
        )

        binding.etDateNaissance.setText(date.format(formatter))

        // Taille et poids
        binding.etTaille.setText(profil.taille.toString())
        binding.etPoids.setText(profil.poids.toString())

        // Sexe et objectif
        selectionnerRadioButton(binding.rgSexe, profil.sexe)
        selectionnerRadioButton(binding.rgObjectif, profil.objectif)

        // Niveau d'activité
        binding.spNiveauActivite.setSelection(
            positionActivite(profil.niveauActivite)
        )

        // Préférences alimentaires
        binding.etPreferencesAlimentaires.setText(
            profil.preferencesAlimentaires ?: ""
        )

        // Allergies et intolérances
        binding.etAllergiesIntolerances.setText(
            profil.allergiesIntolerances ?: ""
        )

        // Calculer l'IMC
        calculerIMC(profil.taille, profil.poids)
    }

    // Sélectionner un RadioButton
    private fun selectionnerRadioButton(
        group: RadioGroup,
        valeur: String
    ) {

        for (i in 0 until group.childCount) {

            val radioButton =
                group.getChildAt(i) as? RadioButton

            if (
                radioButton?.text?.toString()
                    ?.equals(valeur, ignoreCase = true) == true
            ) {
                radioButton.isChecked = true
                return
            }
        }
    }

    // Calculer et afficher l'IMC
    private fun calculerIMC(taille: Double, poids: Double) {

        val tailleMetres = taille / 100.0

        if (tailleMetres > 0) {

            val imc = poids / (tailleMetres * tailleMetres)

            binding.tvIMC.text = String.format(
                Locale.FRANCE,
                "Votre IMC : %.1f",
                imc
            )

            binding.tvIMC.visibility = View.VISIBLE
        }
    }

    // Enregistrer le profil
    private fun enregistrerProfil() {

        // 1. Récupérer les champs
        val dateTexte =
            binding.etDateNaissance.text.toString().trim()

        val taille =
            binding.etTaille.text.toString().trim()

        val poids =
            binding.etPoids.text.toString().trim()

        val preferences =
            binding.etPreferencesAlimentaires.text.toString().trim()

        val allergies =
            binding.etAllergiesIntolerances.text.toString().trim()

        // 2. Récupérer les choix
        val sexeId = binding.rgSexe.checkedRadioButtonId
        val objectifId = binding.rgObjectif.checkedRadioButtonId

        val activitePosition =
            binding.spNiveauActivite.selectedItemPosition

        // 3. Vérifier les champs obligatoires
        if (
            dateTexte.isEmpty() ||
            taille.isEmpty() ||
            poids.isEmpty() ||
            sexeId == -1 ||
            objectifId == -1 ||
            activitePosition == 0
        ) {
            Toast.makeText(
                this,
                "Veuillez remplir tous les champs obligatoires",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // 4. Vérifier la date
        val dateNaissance: LocalDate

        try {

            val formatter = DateTimeFormatter.ofPattern(
                "dd/MM/uuuu",
                Locale.FRANCE
            )

            dateNaissance = LocalDate.parse(dateTexte, formatter)

            if (
                dateNaissance.isAfter(LocalDate.now()) ||
                dateNaissance.isBefore(LocalDate.of(1900, 1, 1))
            ) {
                Toast.makeText(
                    this,
                    "Veuillez saisir une date de naissance valide",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

        } catch (e: DateTimeParseException) {

            Toast.makeText(
                this,
                "Format de date incorrect. Exemple : 15/05/2003",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // 5. Convertir taille et poids
        val tailleValue = taille.replace(',', '.').toDoubleOrNull()
        val poidsValue = poids.replace(',', '.').toDoubleOrNull()

        if (
            tailleValue == null ||
            poidsValue == null ||
            !tailleValue.isFinite() ||
            !poidsValue.isFinite() ||
            tailleValue !in 50.0..250.0 ||
            poidsValue !in 10.0..500.0
        ) {
            Toast.makeText(
                this,
                "Veuillez saisir une taille et un poids valides",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // 6. Récupérer les textes des RadioButton
        val sexe = findViewById<RadioButton>(sexeId)
            .text.toString()

        val objectif = findViewById<RadioButton>(objectifId)
            .text.toString()

        // 7. Récupérer la valeur standard de l'activité
        val activite = activiteValues[activitePosition - 1]

        // 8. Calculer l'IMC
        calculerIMC(tailleValue, poidsValue)

        // 9. Préparer les données
        val request = ProfilRequest(
            dateNaissance = dateNaissance.toString(),
            sexe = sexe,
            taille = tailleValue,
            poids = poidsValue,
            objectif = objectif,
            niveauActivite = activite,
            preferencesAlimentaires = preferences.ifEmpty { null },
            allergiesIntolerances = allergies.ifEmpty { null }
        )

        // 10. Envoyer au backend
        profilViewModel.enregistrerProfil(request)
    }
}
