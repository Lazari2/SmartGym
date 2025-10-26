package com.example.smartgym.viewmodel.register

sealed class RegisterEvent {
    data class OnUsernameChanged(val username: String) : RegisterEvent()
    data class OnEmailChanged(val email: String) : RegisterEvent()
    data class OnPasswordChanged(val password: String) : RegisterEvent()
    data class OnConfirmPasswordChanged(val confirm: String) : RegisterEvent()
    data object OnRegisterClick : RegisterEvent()
}