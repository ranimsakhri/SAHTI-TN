
package tn.rnu.isetr.sahtitn_mobile.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import tn.rnu.isetr.sahtitn_mobile.activities.ProfilActivity
import tn.rnu.isetr.sahtitn_mobile.databinding.FragmentAccueilBinding
import tn.rnu.isetr.sahtitn_mobile.repositories.ProfilRepository
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager
import tn.rnu.isetr.sahtitn_mobile.viewmodels.ProfilViewModel
import tn.rnu.isetr.sahtitn_mobile.viewmodels.ProfilViewModelFactory

class AccueilFragment : Fragment() {

    private var _binding: FragmentAccueilBinding? = null
    private val binding get() = _binding!!

    private lateinit var profilViewModel: ProfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccueilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext().applicationContext

        val repository = ProfilRepository(
            context,
            SessionManager(context)
        )

        profilViewModel = ViewModelProvider(
            this,
            ProfilViewModelFactory(repository)
        )[ProfilViewModel::class.java]

        // Observer le résultat de la récupération du profil
        profilViewModel.profilResult.observe(viewLifecycleOwner) { result ->

            result.onSuccess { profil ->

                val tailleMetres = profil.taille / 100.0
                val imc = profil.poids / (tailleMetres * tailleMetres)

                binding.tvIMC.text = String.format("%.1f kg/m²", imc)

                val categorie = when {
                    imc < 18.5 -> "Insuffisance pondérale"
                    imc < 25 -> "Corpulence normale"
                    imc < 30 -> "Surpoids"
                    else -> "Obésité"
                }

                binding.tvCategorieIMC.text = categorie

                binding.tvDetailsIMC.text =
                    "Poids : ${profil.poids} kg | Taille : ${profil.taille} cm"

                binding.tvObjectif.text = profil.objectif
            }

            result.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    error.message ?: "Impossible de récupérer le profil",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Récupérer le profil depuis Spring Boot
        profilViewModel.recupererProfil()

        // Bouton Modifier le profil (temporaire)
        binding.btnModifierProfil.setOnClickListener {
            startActivity(
                Intent(requireContext(), ProfilActivity::class.java)
            )
        }
        val prenom = arguments?.getString("prenom") ?: ""

        binding.tvBienvenue.text = if (prenom.isNotBlank()) {
            "Mar7be, $prenom ! "
        } else {
            "Mar7be ! "
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
