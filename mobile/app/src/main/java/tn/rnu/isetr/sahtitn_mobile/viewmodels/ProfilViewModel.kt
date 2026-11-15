
package tn.rnu.isetr.sahtitn_mobile.viewmodels

import ProfilRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
//import tn.rnu.isetr.sahtitn_mobile.models.ProfilRequest
import tn.rnu.isetr.sahtitn_mobile.models.ProfilResponse
import tn.rnu.isetr.sahtitn_mobile.repositories.ProfilRepository

class ProfilViewModel(
    private val repository: ProfilRepository
) : ViewModel() {

    private val _profilResult =
        MutableLiveData<Result<ProfilResponse>>()

    val profilResult: LiveData<Result<ProfilResponse>> = _profilResult

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading

    fun enregistrerProfil(request: ProfilRequest) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                _profilResult.value = repository.enregistrerProfil(request)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun recupererProfil() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                _profilResult.value = repository.recupererProfil()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
