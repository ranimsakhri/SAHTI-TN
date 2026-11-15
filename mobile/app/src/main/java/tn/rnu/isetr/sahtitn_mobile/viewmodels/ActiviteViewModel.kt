
package tn.rnu.isetr.sahtitn_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.rnu.isetr.sahtitn_mobile.models.ActiviteRequest
import tn.rnu.isetr.sahtitn_mobile.models.ActiviteResponse
import tn.rnu.isetr.sahtitn_mobile.repositories.ActiviteRepository

class ActiviteViewModel(
    private val repository: ActiviteRepository
) : ViewModel() {

    private val _activites = MutableLiveData<List<ActiviteResponse>>()
    val activites: LiveData<List<ActiviteResponse>> = _activites

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _chargement = MutableLiveData<Boolean>()
    val chargement: LiveData<Boolean> = _chargement

    fun recupererActivites() {
        viewModelScope.launch {
            _chargement.value = true
            val resultat = repository.recupererActivites()

            resultat.onSuccess {
                _activites.value = it
            }.onFailure {
                _message.value = it.message ?: "Erreur lors du chargement des activités"
            }

            _chargement.value = false
        }
    }

    fun ajouterActivite(request: ActiviteRequest, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _chargement.value = true

            val resultat = repository.ajouterActivite(request)

            resultat.onSuccess {
                _message.value = "Activité ajoutée avec succès"
                recupererActivites()
                onResult(true)
            }.onFailure {
                _message.value = it.message ?: "Erreur lors de l'ajout de l'activité"
                onResult(false)
            }

            _chargement.value = false
        }
    }

    fun supprimerActivite(id: Long) {
        viewModelScope.launch {
            val resultat = repository.supprimerActivite(id)

            resultat.onSuccess {
                _message.value = "Activité supprimée avec succès"
                recupererActivites()
            }.onFailure {
                _message.value = it.message ?: "Erreur lors de la suppression"
            }
        }
    }
}
