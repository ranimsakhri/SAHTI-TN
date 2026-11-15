package tn.rnu.isetr.sahtitn_mobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tn.rnu.isetr.sahtitn_mobile.repository.FoodRepository

class FoodViewModelFactory(
    private val repository: FoodRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel inconnu")
    }
}