package com.example.smartgym.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartgym.data.network.LoginRequest
import com.example.smartgym.domain.usecase.LoginUseCase
import com.example.smartgym.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> {
                _uiState.update { it.copy(email = event.email, errorMessage = null) }
            }
            is LoginEvent.OnPasswordChanged -> {
                _uiState.update { it.copy(password = event.password, errorMessage = null) }
            }
            is LoginEvent.OnLoginClick -> {
                loginUser()
            }
        }
    }

    private fun loginUser() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email e senha são obrigatórios.") }
            return
        }

        val loginRequest = LoginRequest(email = email, password = password)

        loginUseCase(loginRequest).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Resource.Success -> {

                    _uiState.update { it.copy(isLoading = false) }

                    val token = result.data?.accessToken

                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.LoginSuccess)
                    }
                }
                is Resource.Error -> {

                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }
}