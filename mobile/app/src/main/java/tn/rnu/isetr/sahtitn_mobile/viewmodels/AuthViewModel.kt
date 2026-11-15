package tn.rnu.isetr.sahtitn_mobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.rnu.isetr.sahtitn_mobile.models.AuthResponse
import tn.rnu.isetr.sahtitn_mobile.models.RegisterRequest
import tn.rnu.isetr.sahtitn_mobile.repositories.AuthRepository
import tn.rnu.isetr.sahtitn_mobile.models.LoginRequest
import tn.rnu.isetr.sahtitn_mobile.models.ChangePasswordRequest
import tn.rnu.isetr.sahtitn_mobile.models.ForgotPasswordRequest
import tn.rnu.isetr.sahtitn_mobile.models.ResetPasswordRequest

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<AuthResponse>>()
    val loginResult: LiveData<Result<AuthResponse>> = _loginResult
    private val _registerResult =
        MutableLiveData<Result<AuthResponse>>()

    val registerResult: LiveData<Result<AuthResponse>> =
        _registerResult

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading
    private val _changePasswordResult = MutableLiveData<Result<String>>()
    val changePasswordResult: LiveData<Result<String>> = _changePasswordResult
    private val _resetPasswordResult = MutableLiveData<Result<String>>()
    val resetPasswordResult: LiveData<Result<String>> = _resetPasswordResult

    private val _forgotPasswordResult = MutableLiveData<Result<String>>()
    val forgotPasswordResult: LiveData<Result<String>> = _forgotPasswordResult
    fun register(request: RegisterRequest) {

        viewModelScope.launch {
            _isLoading.value = true

            val result = repository.register(request)

            _registerResult.value = result
            _isLoading.value = false
        }
    }
    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = repository.login(request)
                _loginResult.value = result
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun changePassword(request: ChangePasswordRequest) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = repository.changePassword(request)
                _changePasswordResult.value = result
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetPassword(request: ResetPasswordRequest) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = repository.resetPassword(request)
                _resetPasswordResult.value = result
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun forgotPassword(request: ForgotPasswordRequest) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = repository.forgotPassword(request)
                _forgotPasswordResult.value = result
            } finally {
                _isLoading.value = false
            }
        }
    }


}
