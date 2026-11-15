package tn.rnu.isetr.sahtitn_mobile.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.rnu.isetr.sahtitn_mobile.models.FoodDto
import tn.rnu.isetr.sahtitn_mobile.repository.FoodRepository


class FoodViewModel(
    private val repository: FoodRepository
) : ViewModel() {

    private val _foodsResult =
        MutableLiveData<Result<List<FoodDto>>>()

    val foodsResult: LiveData<Result<List<FoodDto>>> =
        _foodsResult

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading

    // Récupérer tous les aliments ou rechercher par nom/catégorie
    fun chargerAliments(
        q: String? = null,
        category: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = if (q.isNullOrBlank() && category.isNullOrBlank()) {
                    repository.recupererAliments()
                } else {
                    repository.rechercherAliments(q, category)
                }

                if (response.isSuccessful) {
                    val aliments = response.body() ?: emptyList()
                    _foodsResult.value = Result.success(aliments)
                } else {
                    _foodsResult.value = Result.failure(
                        Exception("Erreur serveur : ${response.code()}")
                    )
                }

            } catch (e: Exception) {
                _foodsResult.value = Result.failure(e)

            } finally {
                _isLoading.value = false
            }
        }
    }
}