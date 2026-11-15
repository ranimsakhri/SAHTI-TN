
package tn.rnu.isetr.sahtitn_mobile.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityForgotPasswordBinding
import tn.rnu.isetr.sahtitn_mobile.models.ForgotPasswordRequest
import tn.rnu.isetr.sahtitn_mobile.repositories.AuthRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModelFactory
import android.content.Intent
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

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

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observerResultats()

        binding.btnSendLink.setOnClickListener {
            envoyerLien()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun envoyerLien() {
        val email = binding.etEmail.text.toString().trim()

        if (email.isBlank()) {
            binding.etEmail.error = "Veuillez saisir votre email"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Adresse email invalide"
            return
        }

        authViewModel.forgotPassword(
            ForgotPasswordRequest(email)
        )
    }

    private fun observerResultats() {
        authViewModel.forgotPasswordResult.observe(this) { result ->

            result.onSuccess { message ->

                Toast.makeText(
                    this,
                    "Un code de réinitialisation a été envoyé à votre adresse e-mail.",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(
                    this,
                    ResetPasswordActivity::class.java
                )

                startActivity(intent)
                finish()
            }

            result.onFailure { erreur ->

                Toast.makeText(
                    this,
                    erreur.message ?: "Erreur lors de l'envoi du code",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        authViewModel.isLoading.observe(this) { loading ->
            binding.btnSendLink.isEnabled = !loading
        }
    }
}
