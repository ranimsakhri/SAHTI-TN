
package tn.rnu.isetr.sahtitn_mobile.activities

import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityResetPasswordBinding
import tn.rnu.isetr.sahtitn_mobile.models.ResetPasswordRequest
import tn.rnu.isetr.sahtitn_mobile.repositories.AuthRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModelFactory

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

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

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observerResultats()

        // Activer les icônes œil
        configurerVisibiliteMotDePasse(binding.etNewPassword)
        configurerVisibiliteMotDePasse(binding.etConfirmPassword)

        binding.btnResetPassword.setOnClickListener {
            reinitialiserMotDePasse()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun reinitialiserMotDePasse() {

        val code = binding.etResetCode.text.toString().trim()

        val nouveauMotDePasse =
            binding.etNewPassword.text.toString().trim()

        val confirmation =
            binding.etConfirmPassword.text.toString().trim()

        // Vérifier le code
        if (code.isBlank()) {
            binding.etResetCode.error =
                "Veuillez saisir le code reçu par e-mail"
            return
        }

        // Vérifier la longueur
        if (nouveauMotDePasse.length < 8) {
            binding.etNewPassword.error =
                "Minimum 8 caractères"
            return
        }

        // Vérifier la présence d'une lettre et d'un chiffre
        if (!nouveauMotDePasse.any { it.isLetter() } ||
            !nouveauMotDePasse.any { it.isDigit() }) {

            binding.etNewPassword.error =
                "Utilisez au moins une lettre et un chiffre"
            return
        }

        // Vérifier la confirmation
        if (nouveauMotDePasse != confirmation) {
            binding.etConfirmPassword.error =
                "Les mots de passe ne correspondent pas"
            return
        }

        // Envoyer le code et le nouveau mot de passe au backend
        authViewModel.resetPassword(
            ResetPasswordRequest(
                token = code,
                newPassword = nouveauMotDePasse
            )
        )
    }

    private fun configurerVisibiliteMotDePasse(champ: EditText) {

        champ.setOnTouchListener { _, event ->

            val drawable = champ.compoundDrawablesRelative[2]

            if (drawable != null &&
                event.action == MotionEvent.ACTION_UP &&
                event.x >= champ.width -
                champ.paddingEnd -
                drawable.bounds.width() - 24) {

                val position = champ.selectionStart

                if (champ.transformationMethod
                            is PasswordTransformationMethod) {

                    // Afficher le mot de passe
                    champ.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()

                } else {

                    // Masquer le mot de passe
                    champ.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                }

                champ.setSelection(
                    position.coerceIn(0, champ.text.length)
                )

                return@setOnTouchListener true
            }

            false
        }
    }

    private fun observerResultats() {

        authViewModel.resetPasswordResult.observe(this) { result ->

            result.onSuccess { message ->

                Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }

            result.onFailure { erreur ->

                Toast.makeText(
                    this,
                    erreur.message ?: "Erreur de réinitialisation",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        authViewModel.isLoading.observe(this) { loading ->

            binding.btnResetPassword.isEnabled = !loading
        }
    }
}
