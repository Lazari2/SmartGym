package com.example.smartgym.viewmodel.login

sealed class LoginEvent {
    data class OnEmailChanged(val email: String) : LoginEvent()
    data class OnPasswordChanged(val password: String) : LoginEvent()
    data object OnLoginClick : LoginEvent()
}

sealed class UiEvent {
    data object LoginSuccess : UiEvent()
    data class ShowError(val message: String) : UiEvent()
}