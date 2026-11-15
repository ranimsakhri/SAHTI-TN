
package tn.rnu.isetr.sahtitn_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityInscriptionBinding
import tn.rnu.isetr.sahtitn_mobile.models.RegisterRequest
import tn.rnu.isetr.sahtitn_mobile.repositories.AuthRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModelFactory
import tn.rnu.isetr.sahtitn_mobile.activities.LoginActivity
class InscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInscriptionBinding

    // Création du ViewModel avec sa Factory
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(
            applicationContext,
            SessionManager(applicationContext)
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observer le résultat de l'inscription
        authViewModel.registerResult.observe(this) { result ->

            result.onSuccess { response ->

                Toast.makeText(
                    this,
                    "Inscription réussie ! Bienvenue ${response.prenom}",
                    Toast.LENGTH_LONG
                ).show()

                // Aller vers le formulaire de profil
                val intent = Intent(
                    this,
                    ProfilActivity::class.java
                )

                startActivity(intent)
                finish()
            }

            result.onFailure { error ->

                Toast.makeText(
                    this,
                    error.message ?: "Erreur lors de l'inscription",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Observer l'état de chargement
        authViewModel.isLoading.observe(this) { loading ->
            binding.btnRegister.isEnabled = !loading
        }

        // Action du bouton
        binding.btnRegister.setOnClickListener {
            inscrireUtilisateur()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.txtGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun inscrireUtilisateur() {

        val nom = binding.etNom.text.toString().trim()
        val prenom = binding.etPrenom.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // Vérification des champs obligatoires
        if (
            nom.isEmpty() ||
            prenom.isEmpty() ||
            email.isEmpty() ||
            password.isEmpty() ||
            confirmPassword.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Veuillez remplir tous les champs",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Vérification du mot de passe
        if (
            password.length < 8 ||
            !password.any { it.isLetter() } ||
            !password.any { it.isDigit() }
        ) {
            Toast.makeText(
                this,
                "Le mot de passe doit contenir 8 caractères, des lettres et des chiffres",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Vérification de la confirmation
        if (password != confirmPassword) {
            Toast.makeText(
                this,
                "Les mots de passe ne correspondent pas",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Préparation de la requête
        val request = RegisterRequest(
            nom = nom,
            prenom = prenom,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )

        // L'Activity appelle uniquement le ViewModel
        authViewModel.register(request)
    }
}
