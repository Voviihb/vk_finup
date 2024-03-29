package com.vk_edu.finup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.finup.data.LoginForm
import com.vk_edu.finup.data.ResponseStatus
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val WRONG_EMAIL_OR_PASSWORD = "Wrong email or password!"
    private val EXCEPTION_OCCURRED = "Exception occurred!"

    private val mainRepo: MainRepo = MainRepo()

    private val _loginFormState = mutableStateOf(LoginForm("", ""))
    val loginFormState: State<LoginForm> = _loginFormState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun login(authFunc: () -> Unit, onLogin: () -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response =
                    mainRepo.authUser(_loginFormState.value.email, _loginFormState.value.password)
                if (response.status == ResponseStatus.Success) {
                    if (response.result) {
                        authFunc()
                        onLogin()
                    } else {
                        onError(WRONG_EMAIL_OR_PASSWORD)
                    }
                    _loading.value = false
                } else {
                    onError((response.status as ResponseStatus.Error).message)
                }
            } catch (e: Exception) {
                onError(e.message ?: EXCEPTION_OCCURRED)
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun emailChanged(value: String) {
        _loginFormState.value = _loginFormState.value.copy(
            email = value
        )
    }

    fun passwordChanged(value: String) {
        _loginFormState.value = _loginFormState.value.copy(
            password = value
        )
    }
}
