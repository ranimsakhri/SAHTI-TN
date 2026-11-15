
package tn.rnu.isetr.sahtitn_mobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tn.rnu.isetr.sahtitn_mobile.repositories.ProfilRepository

class ProfilViewModelFactory(
    private val repository: ProfilRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ProfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfilViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel inconnu")
    }
}
