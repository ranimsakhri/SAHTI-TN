
package tn.rnu.isetr.sahtitn_mobile.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityChangePasswordBinding
import tn.rnu.isetr.sahtitn_mobile.models.ChangePasswordRequest
import tn.rnu.isetr.sahtitn_mobile.repositories.AuthRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModelFactory

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            AuthRepository(
                applicationContext,
                SessionManager(applicationContext)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bouton retour
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Observer le résultat du changement de mot de passe
        authViewModel.changePasswordResult.observe(this) { result ->

            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                // Vider les champs après le succès
                binding.etCurrentPassword.text?.clear()
                binding.etNewPassword.text?.clear()
                binding.etConfirmPassword.text?.clear()

                finish()
            }

            result.onFailure { error ->
                Toast.makeText(
                    this,
                    error.message ?: "Erreur lors du changement de mot de passe",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Gérer le chargement
        authViewModel.isLoading.observe(this) { loading ->
            binding.btnChangePassword.isEnabled = !loading
        }

        // Bouton modifier
        binding.btnChangePassword.setOnClickListener {
            modifierMotDePasse()
        }
    }

    private fun modifierMotDePasse() {

        val ancien = binding.etCurrentPassword.text.toString()
        val nouveau = binding.etNewPassword.text.toString()
        val confirmation = binding.etConfirmPassword.text.toString()

        if (ancien.isBlank() || nouveau.isBlank() || confirmation.isBlank()) {
            Toast.makeText(
                this,
                "Veuillez remplir tous les champs",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (nouveau.length < 8) {
            binding.etNewPassword.error =
                "Le mot de passe doit contenir au moins 8 caractères"
            return
        }

        if (nouveau != confirmation) {
            binding.etConfirmPassword.error =
                "Les mots de passe ne correspondent pas"
            return
        }

        val request = ChangePasswordRequest(
            currentPassword = ancien,
            newPassword = nouveau,
            confirmNewPassword = confirmation
        )

        authViewModel.changePassword(request)
    }
}
