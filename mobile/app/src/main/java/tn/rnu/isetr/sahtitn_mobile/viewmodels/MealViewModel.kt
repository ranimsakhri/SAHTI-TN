
package tn.rnu.isetr.sahtitn_mobile.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.rnu.isetr.sahtitn_mobile.models.CreateMealRequest
import tn.rnu.isetr.sahtitn_mobile.models.MealEntryDto
import tn.rnu.isetr.sahtitn_mobile.repository.MealRepository

class MealViewModel(
    private val repository: MealRepository
) : ViewModel() {

    private val _mealsResult =
        MutableLiveData<Result<List<MealEntryDto>>>()

    val mealsResult: LiveData<Result<List<MealEntryDto>>> =
        _mealsResult

    private val _operationResult =
        MutableLiveData<Result<String>>()

    val operationResult: LiveData<Result<String>> =
        _operationResult

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading

    // Afficher les repas d'une journée
    fun chargerRepas(date: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                chargerRepasInterne(date)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Ajouter un repas
    fun ajouterRepas(request: CreateMealRequest) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = repository.ajouterRepas(request)

                if (response.isSuccessful) {

                    // Actualiser la liste des repas
                    chargerRepasInterne(request.date)

                    _operationResult.value =
                        Result.success("Repas ajouté avec succès")

                } else {
                    _operationResult.value = Result.failure(
                        Exception(
                            "Erreur lors de l'ajout : ${response.code()}"
                        )
                    )
                }

            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)

            } finally {
                _isLoading.value = false
            }
        }
    }

    // Supprimer un repas
    fun supprimerRepas(id: Long, date: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = repository.supprimerRepas(id)

                if (response.isSuccessful) {

                    chargerRepasInterne(date)

                    _operationResult.value =
                        Result.success("Repas supprimé avec succès")

                } else {
                    _operationResult.value = Result.failure(
                        Exception(
                            "Erreur lors de la suppression : ${response.code()}"
                        )
                    )
                }

            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)

            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fonction interne pour récupérer les repas
    private suspend fun chargerRepasInterne(date: String?) {

        val response = repository.recupererRepas(date)

        if (response.isSuccessful) {
            _mealsResult.value =
                Result.success(response.body() ?: emptyList())
        } else {
            _mealsResult.value = Result.failure(
                Exception("Erreur serveur : ${response.code()}")
            )
        }
    }
}
