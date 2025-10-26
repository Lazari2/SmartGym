package com.example.smartgym.viewmodel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartgym.data.network.RegisterRequest
import com.example.smartgym.domain.usecase.RegisterUseCase
import com.example.smartgym.domain.util.Resource
import com.example.smartgym.viewmodel.login.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnUsernameChanged -> _uiState.update { it.copy(username = event.username.trim(), errorMessage = null) }
            is RegisterEvent.OnEmailChanged -> _uiState.update { it.copy(email = event.email.trim(), errorMessage = null) }
            is RegisterEvent.OnPasswordChanged -> _uiState.update { it.copy(password = event.password, errorMessage = null) }
            is RegisterEvent.OnConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = event.confirm, errorMessage = null) }
            is RegisterEvent.OnRegisterClick -> registerUser()
        }
    }
    private fun registerUser() {
        val state = _uiState.value

        if (state.username.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Todos os campos são obrigatórios.") }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(errorMessage = "Formato de e-mail inválido.") }
            return
        }
        if (state.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "A senha deve ter pelo menos 6 caracteres.") }
            return
        }
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "As senhas não conferem.") }
            return
        }

        val request = RegisterRequest(
            username = state.username,
            email = state.email,
            password = state.password,
            confirm_password = state.confirmPassword
        )

        registerUseCase(request).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.RegisterSuccess("Conta criada com sucesso!"))
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }
}