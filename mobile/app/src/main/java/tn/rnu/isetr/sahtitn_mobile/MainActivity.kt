
package tn.rnu.isetr.sahtitn_mobile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tn.rnu.isetr.sahtitn_mobile.activities.InscriptionActivity
import tn.rnu.isetr.sahtitn_mobile.activities.LoginActivity
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cliquer sur la carte Client

        val cardClient =binding.cardClient

        cardClient.setOnClickListener {
            startActivity(Intent(this, InscriptionActivity::class.java))
        }

        // Cliquer sur Se connecter

        val txtLogin=binding.txtLogin
        txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
