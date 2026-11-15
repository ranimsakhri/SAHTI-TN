
package tn.rnu.isetr.sahtitn_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tn.rnu.isetr.sahtitn_mobile.activities.ProfilActivity
import tn.rnu.isetr.sahtitn_mobile.databinding.ActivityDashboardBinding
import tn.rnu.isetr.sahtitn_mobile.fragments.AccueilFragment
import tn.rnu.isetr.sahtitn_mobile.fragments.JournalFragment
import tn.rnu.isetr.sahtitn_mobile.fragments.RecettesFragment
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private var prenom: String = ""

    // Fonction pour créer AccueilFragment avec le prénom
    private fun creerAccueilFragment(): AccueilFragment {
        return AccueilFragment().apply {
            arguments = Bundle().apply {
                putString("prenom", prenom)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prenom = intent.getStringExtra("prenom") ?: ""

        // Afficher Accueil au premier lancement
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, creerAccueilFragment())
                .commit()
        }

        // Navigation inférieure
        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_accueil -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, creerAccueilFragment())
                        .commit()
                    true
                }

                R.id.nav_journal -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, JournalFragment())
                        .commit()
                    true
                }

                R.id.nav_profil -> {
                    startActivity(
                        Intent(this, ProfilActivity::class.java)
                    )
                    true
                }

                R.id.nav_accompagnement -> {
                    Toast.makeText(
                        this,
                        "Accompagnement : bientôt disponible",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                R.id.nav_recettes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, RecettesFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}
