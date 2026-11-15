package tn.rnu.isetr.sahtitn_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import tn.rnu.isetr.sahtitn_mobile.DashboardActivity
import tn.rnu.isetr.sahtitn_mobile.MainActivity
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityLoginBinding
import tn.rnu.isetr.sahtitn_mobile.models.LoginRequest
import tn.rnu.isetr.sahtitn_mobile.repositories.AuthRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.AuthViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        //flèche de retour
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Observer le résultat de la connexion
        authViewModel.loginResult.observe(this) { result ->

            result.onSuccess { response ->
                Toast.makeText(
                    this,
                    "Bienvenue ${response.prenom} !",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("prenom", response.prenom)
                startActivity(intent)
                finish()
            }

            result.onFailure { error ->

                Toast.makeText(
                    this,
                    error.message ?: "Échec de la connexion",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Observer le chargement
        authViewModel.isLoading.observe(this) { loading ->
            binding.btnLogin.isEnabled = !loading
        }

        binding.btnLogin.setOnClickListener {
            connecterUtilisateur()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun connecterUtilisateur() {

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this,
                "Veuillez remplir tous les champs",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val request = LoginRequest(email, password)
        authViewModel.login(request)
    }
}
